package wb;

import java.awt.Point;

/**
 * @brief floor tiles
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Jashank Jeremy {@literal <z5017851@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 * @author Arunabh Mukherjee {@literal <z5120075@cse.unsw.edu.au>}
 * @date May 2017
 */
class FloorTile
extends Tile {
	private GamePiece contents;

	public FloorTile(Point startCoord) {
		super(startCoord);
		this.contents = null;
	}

	public boolean canBeFilled(){
		return true;
	}

	public GamePiece getContents(){
		return contents;
	}

	// TODO ? should this be void
	public void setContents(GamePiece content) {
		contents = content;
	}
}
