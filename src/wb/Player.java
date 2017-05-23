package wb;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * @brief the players!
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 * @date May 2017
 */
public class Player
extends GamePiece {

	private int direction;

	public Player(Board myBoard, Point startCoord) {
		super(myBoard, startCoord);
		this.direction = 2;
	}

	public int getType() {
		return 0;
	}

	public boolean doMove(int direction) {
		Point sourceCoord = super.getCoord();
		Point destCoord = super.getBoard().nearbyPoint(super.getCoord(), direction);

		Tile source = super.getBoard().getPosition(sourceCoord);
		Tile destination = super.getBoard().getPosition(destCoord);

		this.direction = direction;

		if(destination == null || !destination.canBeFilled())
			return false;//Encountered wall

		GamePiece blocking = destination.getContents();
		if(blocking != null && !blocking.bePushed(direction))
			return false;//Encountered unmovable object
		source.setContents(null);
		destination.setContents(this);
		super.setCoord(destCoord);
		prepAnimation(direction);
		return true;
	}
	
	public boolean isMoving () {
		Point2D curr = super.getAnimOffset();
		if (curr.getX() == 0 && curr.getY() == 0) {
			return false;
		}
		return true;
	}

	public boolean bePushed(int direction) {
		return false;
	}

	public int getDirection() {
		return this.direction;
	}

	
}
