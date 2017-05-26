package wb;

import java.awt.Point;

/**
 * cut the world up into tiny pieces
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Jashank Jeremy {@literal <z5017851@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
abstract class Tile {

	private Point thisCoord;

	public Tile(Point startCoord) {
		this.thisCoord = startCoord;
	}

	public Point getCoord(){
		return thisCoord;
	}

	public abstract boolean canBeFilled();

	public abstract GamePiece getContents();

	public abstract void setContents(GamePiece newContents);
}
