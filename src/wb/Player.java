package wb;

public class Player{

	Coord thisCoord;

	public Player(Coord startCoord){
		this.thisCoord = startCoord;
	}

	public Coord getCoord(){
		return thisCoord;
	}

	public boolean canMove(){
		return false;
	}

	public void doMove(){
		
	}

	public void setCoord(Coord updated){
		thisCoord = updated;
	}
}
