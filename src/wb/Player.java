package wb;

import java.awt.*;
import java.awt.geom.Point2D;

public class Player extends GamePiece {

	private int direction;

	public Player(Board myBoard, Point startCoord) {
		super(myBoard, startCoord);
		this.direction = 0;//TODO Make random?
	}

	public int getType() {
		return 0;
	}

	public boolean doMove(int direction) {
		Point sourceCoord = super.getCoord();
		Point destCoord = super.nearbyPoint(direction);

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
