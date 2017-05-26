package wb;

import java.awt.Point;

/**
 * a gameplay crate
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
class Crate
extends GamePiece {

	/**
	 * Constructor
	 *
	 * @param myBoard : The board that this crate belongs to
	 * @param startCoord : The starting point of the crate
	 */
	public Crate(Board myBoard, Point startCoord) {
		super(myBoard, startCoord);
	}

	/**
	 *
	 * @return 1 indicating this is of type crate
	 */
	public int getType() {
		return 1;
	}

	/**
	 *
	 * @return false since crates can only be pushed, not do moves
	 * @param dir : The Direction of attempted movement
	 */
	public boolean doMove(Direction dir) {
		return false;
	}

	/**
	 *
	 * @return true if the crate can be pushed and false otherwise
	 * @param dir : The direction that a player is attempting to push a crate
	 */
	public boolean bePushed(Direction dir) {
		Point sourceCoord = super.getCoord();
		Point destCoord = super.getBoard().nearbyPoint(super.getCoord(), dir);

		Tile source = super.getBoard().getPosition(sourceCoord);
		Tile destination = super.getBoard().getPosition(destCoord);

		if(destination == null || !destination.canBeFilled()){
			return false;//Encountered wall
		}

		GamePiece blocking = destination.getContents();
		if(blocking != null)
			return false;//Encountered another object
		source.setContents(null);
		super.getBoard().addPiecesUndo();
		destination.setContents(this);
		super.setCoord(destCoord);
		prepAnimation(dir);
		return true;
	}

}
