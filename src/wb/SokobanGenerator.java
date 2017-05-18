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

		List<Point> visableWalls = new ArrayList<>();
		for(int i = 0; i < 4; i++) {
			Point neighbour = seed.nearbyPoint(playerPos, i);
			if(neighbour != null)
				visableWalls.add(neighbour);
		}

		List<Point> empty = new ArrayList<>();

		int spaces = (width * height)/2;//TODO add random element
		int crates = (width * height)/10 + 1;//TODO add random element
		//Adding spaces
		seed = clearSpace(seed, spaces, empty, visableWalls);
		//Adding crates
		seed = addCrates(seed, crates, empty);
		//Fill in the ends
		//return fillEnds(seed);
		System.out.println(seed);
		return seed;
	}

	private Board clearSpace(Board seed, int spaces, List<Point> empty, List<Point> visableWalls) {
		if(spaces <= 0)
			return  seed;
		//Expand empty space
		Point chosenWall = visableWalls.get(Math.abs(rand.nextInt()%visableWalls.size()));
		visableWalls.remove(chosenWall);
		empty.add(chosenWall);

		seed.setPosition(chosenWall, new FloorTile(chosenWall));
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

		return clearSpace(seed, spaces-1, empty, visableWalls);
	}

	private Board addCrates(Board seed, int crates, List<Point> empty) {
		if(crates <= 0)
			return seed;
		if(empty.size() == 0)
			return seed;
		Point chosenSpace = empty.get(Math.abs(rand.nextInt()%empty.size()));
		empty.remove(chosenSpace);

		seed.getPosition(chosenSpace).setContents(new Crate(seed, chosenSpace));
		return addCrates(seed, crates-1, empty);
	}

	private Board fillEnds(Board seed) {
		return null;//TODO: IMPLEMENT
	}
}