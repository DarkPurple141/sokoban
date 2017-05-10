package wb;

import java.awt.*;

public abstract class GamePiece {

	private Board myBoard;

	private Point thisCoord;

	public GamePiece(Board myBoard, Point startCoord) {
		this.myBoard = myBoard;
		this.thisCoord = startCoord;
	}

	public Point getCoord() {
		return thisCoord;
	}

	public void setCoord(Point coord) {
		this.thisCoord = coord;
	}

	public abstract int getType();

	public abstract boolean doMove(int direction);


	public abstract boolean bePushed(int direction);

	public Board getBoard() {
		return myBoard;
	}
	
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
}
