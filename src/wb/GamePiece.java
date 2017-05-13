package wb;

import java.awt.*;

public abstract class GamePiece {

	private Board myBoard;

	private Point thisCoord;

	private Point animOffset;

	public GamePiece(Board myBoard, Point startCoord) {
		this.myBoard = myBoard;
		this.thisCoord = startCoord;
		this.animOffset = new Point();
		this.animOffset.setLocation(0.0, 0.0);
	}

	public Point getCoord() {
		return thisCoord;
	}

	public void setCoord(Point coord) {
		this.thisCoord = coord;
	}

	public Point getAnimOffset() {
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

	public Point nearbyPoint(int direction) {
		return useDirection(thisCoord, direction);
	}

	public void prepAnimation(int direction) {
		this.animOffset = useDirection(animOffset, direction);
		System.out.println("x: "+animOffset.getX());
		System.out.println("y: "+animOffset.getY());
	}

	private Point useDirection(Point start, int direction) {
		Point p = new Point();
		double startx = start.getX();
		double starty = start.getY();
		if(direction == 0)
			starty--;
		else if(direction == 1)
			startx++;
		else if(direction == 2)
			starty++;
		else if(direction == 3)
			startx--;
		p.setLocation(startx, starty);
		return p;
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
}
