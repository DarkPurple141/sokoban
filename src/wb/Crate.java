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


	/**
	 * Checks whether a Crate can move in the direction specified.
	 * Probably won't have to be called directly since it is part of the check of whether a
	 * Player can move or not.
	 */ 
	public boolean canMove(int direction, Board gameBoard){
		Tile moveInto = gameBoard.getPosition(thisCoord.getNeighbour(direction));
		return (moveInto != null && moveInto.canBeFilled());
	}

	/**
	 * Moves the Crate in the direction specified
	 * Is called as part of the doMove method for a Player
	 * Therefore probably not need to be called directly
	 */
	public boolean doMove(int direction, Board gameBoard){
		if (canMove(direction, gameBoard)){
			ContainerTile moveInto = (ContainerTile)gameBoard.getPosition(thisCoord.getNeighbour(direction));
			moveInto.setContents(this);
			ContainerTile moveFrom = (ContainerTile)gameBoard.getPosition(thisCoord);
			moveFrom.setContents(null);
			thisCoord = moveInto.getCoord();
			return true;
		}else{
			return false;
		}
	}

	public void setCoord(Coord updated){
		thisCoord = updated;
	}
}
