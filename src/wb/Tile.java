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
	private Point here;

	/**
	 * Sets the position that this tile is situated in
	 * @param start the starting position
	 */
	public Tile(Point start) {
		this.here = start;
	}

	/**
	 * Gets the coordinate of this tile
	 * @return the point coordiante
	 */
	public Point getCoord(){
		return this.here;
	}
	
	public abstract boolean canBeFilled();
	public abstract GamePiece getContents();
	public abstract void setContents(GamePiece newContents);
}
