package wb;

import java.awt.*;

public abstract class Tile {

<<<<<<< HEAD
public abstract class Tile {

	private Point thisCoord;

=======
	private Point thisCoord;

>>>>>>> master
	public Tile(Point startCoord) {
		this.thisCoord = startCoord;
	}

	public Point getCoord(){
		return thisCoord;
	}

	public abstract boolean canBeFilled();

	public abstract GamePiece getContents();

	public abstract boolean setContents(GamePiece newContents);
}