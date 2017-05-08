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

	public boolean canMove(int direction, Board gameBoard){
		Tile moveInto = gameBoard.getNeighbour(thisCoord, direction);
		return (moveInto != null && moveInto.canBeFilled());
	}

	public void doMove(int direction, Board gameBoard){
		ContainerTile moveInto = (ContainerTile)gameBoard.getNeighbour(thisCoord, direction);
		moveInto.setContents(this);
		ContainerTile moveFrom = (ContainerTile)gameBoard.getPosition(thisCoord);
		moveFrom.setContents(null);
		thisCoord = moveInto.getCoord();
	}

	public void setCoord(Coord updated){
		thisCoord = updated;
	}
}
