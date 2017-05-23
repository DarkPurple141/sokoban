package wb;

import java.awt.*;

/**
 * @brief An abstraction of a tile!
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 * @date May 2017
 */
public abstract class Tile {

	private Point thisCoord;

	public Tile(Point startCoord) {
		this.thisCoord = startCoord;
	}

	public Point getCoord(){
		return thisCoord;
	}

	public abstract boolean canBeFilled();

	public abstract GamePiece getContents();

	public abstract boolean setContents(GamePiece newContents);
}