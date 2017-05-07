package wb;

/**
 * Created by Ben on 7/5/17.
 */
public class Crate
{
    private Coord thisCoord;

    public Crate(Coord startCoord) {
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
