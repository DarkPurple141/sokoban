package wb;

import java.util.Iterator;
import java.awt.Point;
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
		sandboxBoard = makeSeed(width, height);
	}

	private Board makeSeed(int width, int height) {
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
		seed = finishSeed(seed, seedActions);
		//Fill in the ends
		//return fillEnds(seed);
		System.out.println(seed);
		return seed;
	}

	private Board finishSeed(Board seed, int remainingActions) {
		if(remainingActions <= 0)
			return  seed;
		//TODO Randomise an action and apply to seed
		return finishSeed(seed, remainingActions-1);
	}

	private Board fillEnds(Board seed) {
		return null;//TODO: IMPLEMENT
	}
}