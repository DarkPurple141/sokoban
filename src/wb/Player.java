package wb;

import java.awt.*;

public class Player extends GamePiece {
	public Player(Point startCoord) {
		super(startCoord);
	}

	public int getType() {
		return 0;
	}

	public boolean doMove(Board gameBoard, int direction) {
		Point sourceCoord = super.getCoord();
		Point destCoord = super.nearbyPoint(gameBoard, direction);

		Tile source = gameBoard.getPosition(sourceCoord);
		Tile destination = gameBoard.getPosition(destCoord);

		if(!destination.canBeFilled())
			return false;//Encountered wall

		GamePiece blocking = destination.getContents();
		if(blocking != null && !blocking.bePushed(gameBoard, direction))
			return false;//Encountered unmovable object
		source.setContents(null);
		destination.setContents(this);
		super.setCoord(destCoord);
		return true;
	}

	public boolean bePushed(Board gameBoard, int direction) {
		return false;
	}
}
