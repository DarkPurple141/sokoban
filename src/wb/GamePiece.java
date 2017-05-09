package wb;

import java.awt.*;

public abstract class GamePiece
{
	private Point thisCoord;

	public GamePiece(Point startCoord) {
		this.thisCoord = startCoord;
	}

	public Point getCoord() {
		return thisCoord;
	}

	public void setCoord(Point coord) {
		this.thisCoord = coord;
	}

	public abstract int getType();

	public abstract boolean doMove(Board gameBoard, int direction);

	public abstract boolean bePushed(Board gameBoard, int direction);

	protected Point nearbyPoint(Board gameBoard, int direction) {
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
}
