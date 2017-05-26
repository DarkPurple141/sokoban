package wb;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * a player on the game board
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
class Player
extends GamePiece {

	private Direction direction;

	public Player(Board b, Point start) {
		super(b, start);
		this.direction = Direction.DOWN;
	}

	public int getType() {
		return 0;
	}

	public boolean doMove(Direction dir) {
		Point sourceCoord = super.getCoord();
		Point destCoord = super.getBoard().nearbyPoint(super.getCoord(), dir);

		Tile source = super.getBoard().getPosition(sourceCoord);
		Tile destination = super.getBoard().getPosition(destCoord);

		this.direction = dir;

		if (destination == null || !destination.canBeFilled()) {
			// Encountered wall
			return false;
		}

		GamePiece blocking = destination.getContents();
		if (blocking != null && !blocking.bePushed(dir)) {
			// Encountered unmovable object
			return false;
		}

		source.setContents(null);
		destination.setContents(this);
		super.setCoord(destCoord);
		prepAnimation(dir);
		return true;
	}

	public boolean isMoving () {
		Point2D curr = super.getAnimOffset();
		if (curr.getX() == 0 && curr.getY() == 0) {
			return false;
		}
		return true;
	}

	public boolean bePushed(Direction direction) {
		return false;
	}

	public Direction getDirection() {
		return this.direction;
	}
}
