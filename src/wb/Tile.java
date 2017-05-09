package wb;

public class Tile {

	private Coord thisCoord;

	public Tile(Coord startCoord) {
		this.thisCoord = startCoord;
	}

	public Coord getCoord(){
		return thisCoord;
	}

	public boolean canBeFilled(){
		return false;
	}
}
