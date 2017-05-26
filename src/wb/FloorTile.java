package wb;

import java.awt.Point;

/**
 * A floor tile is a type of tile which can have a GamePiece atop it.
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
class FloorTile
extends Tile {
	private GamePiece contents;

	/**
	 * Create a new FloorTile.
	 *
	 * @param start the initial coördinate.
	 */
	public FloorTile (Point start) {
		super(start);
		this.contents = null;
	}

	/**
	 * Can this tile be filled: can we place a GamePiece atop it?
	 *
	 * @return true, by definition.
	 */
	public boolean canBeFilled() {
		return true;
	}

	/**
	 * Returns the contents of this tile.
	 *
	 * @return the tile's current contents,
	 *     or null if the tile is vacant.
	 */
	public GamePiece getContents() {
		return this.contents;
	}

	/**
	 * Updates the contents of this tile.
	 *
	 * @param contents the new tile contents
	 */
	public void setContents (GamePiece contents) {
		this.contents = contents;
	}
}
