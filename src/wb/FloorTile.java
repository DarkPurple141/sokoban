package wb;

import java.awt.*;

public class FloorTile extends Tile
{
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

	public boolean setContents(GamePiece content) {
		contents = content;
		return true;
	}
}
