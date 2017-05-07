package wb;

public class Box{
	
	Coord coord;

	public Box(Coord coord){
		this.coord = coord;
	}

	public Coord getCoord(){
		return coord;
	}

	public boolean canMove(){
		return false;
	}

	public void doMove(){
		
	}
}