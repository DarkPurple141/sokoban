package wb;

import java.awt.*;

/**
 * @brief Well, well, well, three holes in the ground.
 *
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @date May 2017
 */
public class Wall
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

	public boolean setContents(GamePiece newContents) {
		return false;
	}
}
