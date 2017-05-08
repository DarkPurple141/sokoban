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
	 */
	public boolean canMove(int direction, Board gameBoard){
		Tile moveInto = gameBoard.getNeighbour(thisCoord, direction);
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

	public void doMove(int direction, Board gameBoard){
		ContainerTile moveInto = (ContainerTile)gameBoard.getNeighbour(thisCoord, direction);
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
