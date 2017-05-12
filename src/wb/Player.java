package wb;

import java.awt.*;

public class Player extends GamePiece {
	public Player(Board myBoard, Point startCoord) {
		super(myBoard, startCoord);
	}

	public int getType() {
		return 0;
	}

	public boolean doMove(int direction) {
		Point sourceCoord = super.getCoord();
		Point destCoord = super.nearbyPoint(direction);

		Tile source = super.getBoard().getPosition(sourceCoord);
		Tile destination = super.getBoard().getPosition(destCoord);

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

	public boolean bePushed(int direction) {
		return false;
	}
}
