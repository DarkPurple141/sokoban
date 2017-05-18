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

	public void resetBoard(){
		Iterator<Tile> boardIterator = sandboxBoard.tileIterator();
		Player player = sandboxBoard.getPlayers().get(0);
		while (boardIterator.hasNext()){
			Tile currentTile = boardIterator.next();
			Point currentCoord = currentTile.getCoord();
			if (currentCoord.x == sandboxBoard.getWidth()/2 && currentCoord.y == sandboxBoard.getHeight()/2){
				currentTile.setContents(player);
				player.setCoord(currentCoord);
			}else{
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
		playerPos.setLocation(rand.nextInt()%width, rand.nextInt()%height);
		Tile playerTile = new FloorTile(playerPos);
		playerTile.setContents(new Player(seed, playerPos));
		seed.setPosition(playerPos, playerTile);
		//Finish making the seed
		int seedActions = (width * height)/2;//This can be changed later

		List<Point> visableWalls = new ArrayList<>();
		for(int i = 0; i < 4; i++) {
			
		}
		seed = finishSeed(seed, seedActions, playerPos, new ArrayList<>(), visableWalls);
		//Fill in the ends
		return fillEnds(seed);
	}

	private Board finishSeed(Board seed, int remainingActions, Point player, List<Point> empty, List<Point> visableWalls) {
		if(remainingActions <= 0)
			return  seed;
		return finishSeed(seed, remainingActions-1, player, empty, visableWalls);
	}

	private Board fillEnds(Board seed) {
		return null;//TODO: IMPLEMENT
	}
}