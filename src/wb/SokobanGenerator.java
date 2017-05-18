package wb;

import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Point;
import java.util.List;
import java.util.Random;

public class SokobanGenerator{

	private Board sandboxBoard;

	private Random rand = new Random();

	// PLAN - Generate a level with all walls and player in centre
	// This is base state
	// Children nodes are:
	// 1. Delete a wall
	// 2. Place a box
	// 3. Freeze the level (save)
	// After these phases, the level is saved so move actions can be generated as children nodes
	// 4. Move the player
	// 5. Evaluate the Level
	public SokobanGenerator(int width, int height){
		sandboxBoard = generateLevel(width, height);
	}

	public void resetBoard() {
		Iterator<Tile> boardIterator = sandboxBoard.tileIterator();
		Player player = sandboxBoard.getPlayers().get(0);
		while (boardIterator.hasNext()) {
			Tile currentTile = boardIterator.next();
			Point currentCoord = currentTile.getCoord();
			if (currentCoord.x == sandboxBoard.getWidth() / 2 && currentCoord.y == sandboxBoard.getHeight() / 2) {
				currentTile.setContents(player);
				player.setCoord(currentCoord);
			} else {
				currentTile.setContents(null);
			}
		}
	}

	private Board generateLevel(int width, int height) {
		Board seed = new Board(width, height);

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
		int x = rand.nextInt(Integer.MAX_VALUE)%width;
		int y = rand.nextInt(Integer.MAX_VALUE)%height;
		playerPos.setLocation(x, y);
		Tile playerTile = new FloorTile(playerPos);
		playerTile.setContents(new Player(seed, playerPos));
		System.out.println(playerPos);
		seed.setPosition(playerPos, playerTile);
		//Finish making the seed
		int seedActions = (width * height)/2;//This can be changed later

		List<Point> visableWalls = new ArrayList<>();
		for(int i = 0; i < 4; i++) {
			Point neighbour = seed.nearbyPoint(playerPos, i);
			if(neighbour != null)
				visableWalls.add(neighbour);
		}
		seed = finishSeed(seed, seedActions, new ArrayList<>(), visableWalls);
		//Fill in the ends
		//return fillEnds(seed);
		System.out.println(seed);
		return seed;
	}

	private Board finishSeed(Board seed, int remainingActions, List<Point> empty, List<Point> visableWalls) {
		if(remainingActions <= 0)
			return  seed;
		int action = rand.nextInt()%2;//TODO Make this weighted
		if(empty.size() == 0)//If no empty spots, force an expansion
			action = 0;
		if(action == 0) {
			//Expand empty space
			Point chosenWall = visableWalls.get(rand.nextInt()%visableWalls.size());
			seed.setPosition(chosenWall, new FloorTile(chosenWall));
			empty.add(chosenWall);
			for(int i = 0; i < 4; i++) {
				Point neighbour = seed.nearbyPoint(chosenWall, i);
				if(neighbour == null)
					continue;
				if(empty.contains(neighbour) || visableWalls.contains(neighbour))
					continue;
				if(seed.getPosition(neighbour).getContents() != null)
					continue;
				visableWalls.add(neighbour);
			}
		} else {
			//Add a box
			Point chosenSpace = empty.get(rand.nextInt()%empty.size());
			seed.getPosition(chosenSpace).setContents(new Crate(seed, chosenSpace));
			empty.remove(chosenSpace);
		}

		return finishSeed(seed, remainingActions-1, empty, visableWalls);
	}

	private Board fillEnds(Board seed) {
		return null;//TODO: IMPLEMENT
	}
}