package wb;

import java.awt.Point;

/**
 * Unlike a FloorTile, a WallTile may not have a GamePiece atop it,
 * and cannot be moved onto.
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 */
class WallTile
extends Tile {
	/**
	 * Create a new WallTile.
	 * @param start the initial coördinate.
	 */
	public WallTile(Point start) {
		super(start);
	}

	/**
	 * Can this tile contain a piece?
	 * @return false
	 */
	public boolean canBeFilled() {
		return false;
	}

	/**
	 * Returns the contents of this tile; by definition, a WallTile may
	 * never have any content, so we return null.
	 *
	 * @return null
	 */
	public GamePiece getContents() {
		return null;
	}

	/**
	 * Silently fails to set the Tile's contents.
	 *
	 * @param newContents the contents to set
	 */
	public void setContents (GamePiece newContents) {
	}
}
