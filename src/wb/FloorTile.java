package wb;

import java.awt.Point;

/**
 * @brief A floor tile.
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @date May 2017
 */
public class FloorTile extends Tile {
	private GamePiece contents;

	public FloorTile(Point startCoord) {
		super(startCoord);
		this.contents = null;
	}

	public boolean canBeFilled() {
		return true;
	}

	public GamePiece getContents() {
		return this.contents;
	}

	// TODO ? should this be void
	public boolean setContents(GamePiece content) {
		this.contents = content;
		return true;
	}
}
