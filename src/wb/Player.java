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

		if(moveInto == null || !(moveInto instanceof  ContainerTile))
			return false;

		Object existingContents = ((ContainerTile)moveInto).getContents();
		if(existingContents == null || !(existingContents instanceof  Crate))
			return false;

		if (!((Crate)existingContents).canMove(direction, gameBoard))
			return false;

		return true;
	}

	/**
	 * Moves the player, and box if necessary, in the direction indicated by the
	 * 'direction' param.
	 *
	 * Does no validation on the move and therefore must always be preceded by a call to canMove()
	 * with the same direction
	 *
	 * The method also moves an adjacent crate if necessary
	 */
	public boolean doMove(int direction, Board gameBoard){

		if(!canMove(direction, gameBoard))
			return false;

		ContainerTile moveInto = (ContainerTile)gameBoard.getPosition(thisCoord.getNeighbour(direction));

		Object pushing = moveInto.getContents();

		if(pushing instanceof Player)
			return false;

		if(!((Crate)pushing).doMove(direction, gameBoard))
			return false;

		moveInto.setContents(this);
		ContainerTile moveFrom = (ContainerTile)gameBoard.getPosition(thisCoord);
		moveFrom.setContents(null);
		thisCoord = moveInto.getCoord();

		return true;
	}

	public void setCoord(Coord updated){
		thisCoord = updated;
	}
}
