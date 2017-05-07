package wb;

public class Player{

	Coord coord;

	public Player(Coord coord){
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