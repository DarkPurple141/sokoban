package wb;

import java.awt.Point;

public class FloorTile extends Tile {
	private GamePiece contents;

	public FloorTile(Point startCoord) {
		super(startCoord);
		this.contents = null;
	}

	public boolean canBeFilled(){
		return true;
	}

	public GamePiece getContents(){
		return contents;
	}

	// TODO ? should this be void
	public void setContents(GamePiece content) {
		contents = content;
	}
}
