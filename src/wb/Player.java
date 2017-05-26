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

	/**
	 * Create a new Player!
	 *
	 * @param b the board this player exists on
	 * @param start the starting coördinate for this player
	 */
	public Player(Board b, Point start) {
		super(b, start);
		this.direction = Direction.DOWN;
	}

	/**
	 * Specifies the type of this GamePiece.
	 *
	 * @return 0 for this type of GamePiece.
	 */
	public int getType() {
		return 0;
	}

	/**
	 * Move this player in a direction.
	 *
	 * @param dir the direction to move the player in.
	 * @return true if the player could be moved; false otherwise.
	 */
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

	/**
	 * Is this piece currently moving?
	 *
	 * @return true if the current animation offset, which is used to
	 *     determine the player's rendered position and sprite, is
	 *     non-zero; false otherwise.
	 */
	public boolean isMoving () {
		Point2D curr = super.getAnimOffset();
		if (curr.getX() == 0 && curr.getY() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Can this piece be pushed in this direction?  (Spoiler: no.)
	 *
	 * @param dir the direction to attempt a move in.
	 * @return false: you can't push players.
	 */
	public boolean bePushed (Direction dir) {
		return false;
	}

	/**
	 * What direction is this piece facing in?
	 *
	 * @return a Direction representing the current orientation of
	 *     this piece.
	 */
	public Direction getDirection() {
		return this.direction;
	}
}
