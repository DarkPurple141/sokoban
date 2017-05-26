package wb;

import java.awt.Point;

/**
 * an immovable object
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 */
class Wall
extends Tile {
	/**
	 * Initialises the super class
	 * @param start
	 */
	public Wall(Point start) {
		super(start);
	}

	/**
	 * Can this tile contain a piece
	 * @return false
	 */
	public boolean canBeFilled() {
		return false;
	}

	/**
	 * Returns the contents of this tile
	 * Wall can never be filled
	 * @return null
	 */
	public GamePiece getContents() {
		return null;
	}

	/**
	 * Attempts to set the Tile's contents
	 * Silently fails
	 * @param newContents the contents to set
	 */
	public void setContents(GamePiece newContents) {
		// FIXME(jashankj): probably want to throw an exception here
	}
}
