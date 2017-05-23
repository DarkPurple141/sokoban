package wb;

import java.awt.*;

public class Wall extends Tile {
	public Wall(Point startCoord) {
		super(startCoord);
	}

	public boolean canBeFilled() {
		return false;
	}

	public GamePiece getContents() {
		return null;
	}

	public void setContents(GamePiece newContents) {
	}
}
