package wb;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;

/**
 * movable game pieces
 *
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
abstract class GamePiece {
	private Board b;
	private Point here;
	private ArrayDeque<Point> prevCoords;
	private Point2D animOffset;

	/**
	 * Initialises the gamepiece with it's home board and position
	 * @param b tye board which contains this piece
	 * @param start the start position of the gamepiece
	 */
	public GamePiece(Board b, Point start) {
		this.b = b;
		this.here = start;
		this.prevCoords = new ArrayDeque<Point>();
		this.animOffset = new Point2D.Double();
		this.animOffset.setLocation(0.0, 0.0);
	}

	/**
	 * Returns the coorinates of the gamepiece
	 * @return Point coords of this gamepiece
	 */
	public Point getCoord() {
		return this.here;
	}

	/**
	 * Sets the gamepiece to a new position
	 * @param coord the position to set the gamepiece
	 */
	public void setCoord(Point coord) {
		this.here = coord;
	}

	/**
	 * Returns the floating point coordinates for the difference between the piece's apparent position and its actual position
	 * @return Point of doubles for the animation offset
	 */
	public Point2D getAnimOffset() {
		return animOffset;
	}

	/**
	 * Moves the piece's apparent position a given distance closer to the actual position
	 * @param step the distance closer to move the animation position
	 */
	public void animFrame(double step) {
		double movex = clipDouble(animOffset.getX(), step);
		double movey = clipDouble(animOffset.getY(), step);
		this.animOffset.setLocation(movex, movey);
	}

	/**
	 * Returns the type of piece this is
	 * 0 = player
	 * 1 = crate
	 * @return int for the type of piece
	 */
	public abstract int getType();

	/**
	 * Makes the piece attempt to move in the given direction
	 * @param dir the direction to move in
	 * @return true if the move was successful
	 */
	public abstract boolean doMove(Direction dir);

	/**
	 * Attempts to be pushed in the given direction
	 * @param dir the direction to be pushed in
	 * @return true if the piece could be pushed
	 */
	public abstract boolean bePushed(Direction dir);

	/**
	 * Returns the board containing this piece
	 * @return
	 */
	public Board getBoard() {
		return this.b;
	}

	/**
	 * Moves the apparent position of the piece 1 unit in a given direction
	 * For beginning the animation process
	 * @param dir the direction to set the animation offset in
	 */
	public void prepAnimation(Direction dir) {
		double startx = animOffset.getX();
		double starty = animOffset.getY();
		if(dir == Direction.UP)
			starty++;
		else if(dir == Direction.RIGHT)
			startx--;
		else if(dir == Direction.DOWN)
			starty--;
		else if(dir == Direction.LEFT)
			startx++;
		animOffset.setLocation(startx, starty);
	}

	/**
	 * A utility function to move the double step units closer to 0
	 * Otherwise clip doubles to 0 if they are close to 0
	 * @param origin the double to clip
	 * @param step the step to move closer by
	 * @return the stepped/clipped double
	 */
	private double clipDouble(double origin, double step) {
		if (origin < 0) {
			origin += step;
			if(origin > 0)
				origin = 0;
		} else if(origin > 0) {
			origin -= step;
			if(origin < 0)
				origin = 0;
		}
		return origin;
	}

	/**
	 * Push a move onto the undo stack
	 */
	public void storePrevCoord() {
		if (prevCoords.size() >= Board.UNDO_LENGTH) {
			prevCoords.pollLast();
		}
		prevCoords.push(this.here);
	}

	/**
	 * Undoes one move by this piece
	 */
	public void undo() {
		if (prevCoords.isEmpty()){
			return;
		}
		this.here = prevCoords.pop();
	}

}
