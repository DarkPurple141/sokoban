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
		int newx = thisCoord.x;
		int newy = thisCoord.y;
		switch (direction) {
			case 0://Up
				newy--;
				break;
			case 1://Right
				newx++;
				break;
			case 2://Down
				newy++;
				break;
			case 3://Left
				newx--;
				break;
		}
		return new Point(newx, newy);
	}

	public void prepAnimation(int direction)
	{
		double animx = animOffset.getX();
		double animy = animOffset.getY();
		switch (direction)
		{
			case 0://Up
				animy--;
				break;
			case 1://Right
				animx++;
				break;
			case 2://Down
				animy++;
				break;
			case 3://Left
				animx--;
		}
		this.animOffset.setLocation(animx, animy);
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
