package wb;

import java.util.Iterator;
import java.awt.Point;	
public class SokobanGenerator{

	private Board sandboxBoard;
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
		sandboxBoard = new Board(width, height);
		resetBoard();
	}

	public void resetBoard(){
		Iterator<Tile> boardIterator = sandboxBoard.iterator();
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

	private Board makeSeed(int width, int height) {
		return null;//TODO: IMPLEMENT
	}

	private Board finishSeed(Board seed, int remainingActions) {
		if(remainingActions <= 0)
			return  seed;
		//TODO Randomise an action and apply to seed
		return finishSeed(seed, remainingActions-1);
	}

	private fillEnds(Board seed) {
		return null;//TODO: IMPLEMENT
	}
}