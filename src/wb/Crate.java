package wb;

import java.awt.Point;

/**
 * @brief A particular piece on the board.
 *
 * @author Jashank Jeremy {@literal <z5017851@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 * @date May 2017
 */
class Crate
extends GamePiece {
	private int id;
	public Crate(Board myBoard, Point startCoord) {
		super(myBoard, startCoord);
		id = myBoard.getCrates().size();
	}

	public int getType() {
		return 1;
	}

	public boolean doMove(int direction) {
		return false;
	}

	public boolean bePushed(int direction) {
		Point sourceCoord = super.getCoord();
		Point destCoord = super.getBoard().nearbyPoint(super.getCoord(), direction);

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
		prepAnimation(direction);
		return true;
	}

	public void setID(int newID){
		id = newID;
	}

	
}
