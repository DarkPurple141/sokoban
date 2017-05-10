package wb;

import java.awt.*;

public class Crate extends GamePiece {
	public Crate(Point startCoord) {
		super(startCoord);
	}

	public int getType() {
		return 1;
	}

	public boolean doMove(Board gameBoard, int direction) {
		return false;
	}

	public boolean bePushed(Board gameBoard, int direction) {
		Point sourceCoord = super.getCoord();
		Point destCoord = super.nearbyPoint(gameBoard, direction);

		Tile source = gameBoard.getPosition(sourceCoord);
		Tile destination = gameBoard.getPosition(destCoord);

		if(destination == null || !destination.canBeFilled()){
			return false;//Encountered wall
		}

		GamePiece blocking = destination.getContents();
		if(blocking != null){
			
			return false;//Encountered another object
		}
		source.setContents(null);
		destination.setContents(this);
		super.setCoord(destCoord);
		return true;
	}
}
