package wb;

import java.awt.Point;

/**
 * @brief an immovable object
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @date May 2017
 */
class Wall
extends Tile {
	public Wall(Point startCoord) {
		super(startCoord);
	}

	public boolean canBeFilled() {
		return false;
	}

	public GamePiece getContents() {
		return null;
	}

	public void setContents(GamePiece newContents) {
		// FIXME(jashankj): probably want to throw an exception here
	}
}
