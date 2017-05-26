package wb;

import java.util.ArrayList;
import java.awt.Point;
import java.util.List;
import java.util.Random;

/**
 * a Sokoban-style game generator
 *
 * uses a monte-carlo tree simulation, which seems to be the solution
 * par excellence for this problem.
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
class SokobanGenerator {

	private static final int alpha = 10;
	private static final int beta = 1;
	private static final int gamma = 4;
	private static final int tau = 1;

	private static final double blockDensity = 0.4;
	private static final double crateDensity = 0.3;

	private static final int tries = 15;

	private static Random rand = new Random();

	private static double lastBestScore = 0;

	// PLAN - Generate a level with all walls and player in centre
	// This is base state
	// Children nodes are:
	// 1. Delete a wall
	// 2. Place a box
	// 3. Freeze the level (save)
	// After these phases, the level is saved so move actions can be generated as children nodes
	// 4. Move the player
	// 5. Evaluate the Level

//	public static void resetBoard() {
//		Iterator<Tile> boardIterator = sandboxBoard.tileIterator();
//		Player player = sandboxBoard.getPlayers().get(0);
//		while (boardIterator.hasNext()) {
//			Tile currentTile = boardIterator.next();
//			Point currentCoord = currentTile.getCoord();
//			if (currentCoord.x == sandboxBoard.getWidth() / 2 && currentCoord.y == sandboxBoard.getHeight() / 2) {
//				currentTile.setContents(player);
//				player.setCoord(currentCoord);
//			} else {
//				currentTile.setContents(null);
//			}
//		}
//	}

	public static void generateLevel(int width, int height, int id) {

		Board[] selection = new Board[tries];
		double[] scores = new double[tries];

		//Fill the array with completed boards
		for(int i = 0; i < tries; i++) {
			selection[i] = completeSeed(new Board(width, height), width, height);
			scores[i] = lastBestScore;
		}
		//Pick the best of the bunch
		Board best = null;
		double bestScore = 0;

		for(int i = 0; i < tries; i++) {
			if(scores[i] > bestScore) {
				best = selection[i];
				bestScore = scores[i];
				System.out.println(bestScore);
			}
		}

		System.out.println(best);

		FileIO.saveGame(best, "levels/"+Integer.toString(id));
	}

	private static Board completeSeed(Board seed, int width, int height) {
		//Make board full of walls
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				Point pos = new Point();
				pos.setLocation(x, y);
				seed.setPosition(pos, new Wall(pos));
			}
		}
		//Add make a tile with a player in it
		Point playerPos = new Point();
//		int playerx = rand.nextInt(Integer.MAX_VALUE)%width;
//		int playery = rand.nextInt(Integer.MAX_VALUE)%height;
		int playerx = width/2;
		int playery = height/2;
		playerPos.setLocation(playerx, playery);
		Tile playerTile = new FloorTile(playerPos);
		playerTile.setContents(new Player(seed, playerPos));
		//System.out.println(playerPos);
		seed.setPosition(playerPos, playerTile);

		List<Point> visableWalls = new ArrayList<>();
		for(int i = 0; i < 4; i++) {
			Point neighbour = seed.nearbyPoint(playerPos, i);
			if(neighbour != null)
				visableWalls.add(neighbour);
		}
		//determine number of each thing that we want
		int spaces = (int)((width*height)*(1-blockDensity)+2);
		int crates = (int)(spaces*crateDensity);
		//Adding spaces
		seed = clearSpace(seed, spaces, visableWalls);
		//Building list of places where crates can be placed
		List<Point> empty = new ArrayList<>();
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				Point pos = new Point();
				pos.setLocation(x, y);
				if(!seed.getPosition(pos).canBeFilled())
					continue;
				int surroundingWalls = 0;
				for(int i = 0; i < 4; i++) {
					Point neighbour = seed.nearbyPoint(pos, i);
					if(neighbour == null)
						surroundingWalls++;
					else if(!seed.getPosition(neighbour).canBeFilled())
						surroundingWalls++;
				}
				if(surroundingWalls >= 2)
					continue;
				if(seed.getPosition(pos).getContents() != null)
					continue;
				empty.add(pos);
			}
		}
		//Adding crates
		seed = addCrates(seed, crates, empty);
		//Fill in the ends
		seed = fillEnds(seed);
		//System.out.println(seed);
		return seed;
	}

	private static Board clearSpace(Board seed, int spaces, List<Point> visableWalls) {
		if(spaces <= 0)
			return seed;
		if(visableWalls.size() == 0)
			return seed;
		//Expand empty space
		Point chosenWall = visableWalls.get(Math.abs(rand.nextInt()%(visableWalls.size()-1)));
		visableWalls.remove(chosenWall);

		seed.setPosition(chosenWall, new FloorTile(chosenWall));
		for(int i = 0; i < 4; i++) {
			Point neighbour = seed.nearbyPoint(chosenWall, i);
			if(neighbour == null)
				continue;
			if(seed.getPosition(neighbour).canBeFilled())
				continue;
			if(visableWalls.contains(neighbour))
				continue;
			visableWalls.add(neighbour);
		}

		return clearSpace(seed, spaces-1, visableWalls);
	}

	private static Board addCrates(Board seed, int crates, List<Point> empty) {
		if(crates <= 0)
			return seed;
		if(empty.size() == 0)
			return seed;
		Point chosenSpace = empty.get(Math.abs(rand.nextInt()%empty.size()));
		empty.remove(chosenSpace);
		Crate placing = new Crate(seed, chosenSpace);
		seed.getPosition(chosenSpace).setContents(placing);
		seed.getCrates().add(placing);
		return addCrates(seed, crates-1, empty);
	}

	private static Board fillEnds(Board seed) {
		MctsTree decisions = new MctsTree(seed, alpha, beta, gamma, tau);
		Board finished = decisions.scrambleRecurse();
		lastBestScore = decisions.getBestScore();
		return finished;
	}
}
