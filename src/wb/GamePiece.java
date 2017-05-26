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
	private Board myBoard;
	private Point thisCoord;
	private ArrayDeque<Point> prevCoords;
	private Point2D animOffset;

	public GamePiece(Board myBoard, Point startCoord) {
		this.myBoard = myBoard;
		this.thisCoord = startCoord;
		this.prevCoords = new ArrayDeque<Point>();
		this.animOffset = new Point2D.Double();
		this.animOffset.setLocation(0.0, 0.0);
	}

	public Point getCoord() {
		return thisCoord;
	}

	public void setCoord(Point coord) {
		this.thisCoord = coord;
	}

	public Point2D getAnimOffset() {
		return animOffset;
	}

	public void animFrame(double step) {
		double movex = clipDouble(animOffset.getX(), step);
		double movey = clipDouble(animOffset.getY(), step);
		this.animOffset.setLocation(movex, movey);
	}

	public abstract int getType();

	public abstract boolean doMove(int direction);

	public abstract boolean bePushed(int direction);

	public Board getBoard() {
		return myBoard;
	}

	//Public methods below this line are only used by subclasses//

	public void prepAnimation(int direction) {
		double startx = animOffset.getX();
		double starty = animOffset.getY();
		if(direction == 0)
			starty++;
		else if(direction == 1)
			startx--;
		else if(direction == 2)
			starty--;
		else if(direction == 3)
			startx++;
		animOffset.setLocation(startx, starty);
	}

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

	public void storePrevCoord() {
		if (prevCoords.size() >= Board.UNDO_LENGTH) {
			prevCoords.pollLast();
		}
		prevCoords.push(thisCoord);
	}

	public void undo() {
		if (prevCoords.isEmpty()){
			return;
		}
		thisCoord = prevCoords.pop();
	}

}
