package wb;

public class Player{

	private Coord thisCoord;

	public Player(Coord startCoord){
		this.thisCoord = startCoord;
	}

	public Coord getCoord(){
		return thisCoord;
	}

	/**
	 * Determines whether or not a player can move in
	 * a certain direction
	 *
	 * Must always be called before doMove()!!
	 *
	 * This call takes into account an adjacent crate
	 */
	public boolean canMove(int direction, Board gameBoard){
		Tile moveInto = gameBoard.getPosition(thisCoord.getNeighbour(direction));

		if (moveInto != null && moveInto instanceof ContainerTile){
			Object existingContents = ((ContainerTile)moveInto).getContents();
			if(existingContents != null){
				if(existingContents instanceof Crate){
					if (!((Crate)existingContents).canMove(direction, gameBoard)){
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Moves the player, and box if necessary, in the direction indicated by the 
	 * 'direction' param.
	 *
	 * Does no validation on the move and therefore must always be preceded by a call to canMove()
	 * with the same direction
	 *
	 * The method also moves an adjacent crate if necessary
	 *
	 */
	public void doMove(int direction, Board gameBoard){
		ContainerTile moveInto = (ContainerTile)gameBoard.getPosition(thisCoord.getNeighbour(direction));
		Object pushing = moveInto.getContents();
		if (pushing != null && pushing instanceof Crate){
			((Crate)pushing).doMove(direction, gameBoard);
		}
		moveInto.setContents(this);
		ContainerTile moveFrom = (ContainerTile)gameBoard.getPosition(thisCoord);
		moveFrom.setContents(null);
		thisCoord = moveInto.getCoord();
	}

	public void setCoord(Coord updated){
		thisCoord = updated;
	}
}
