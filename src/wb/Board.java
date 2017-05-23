package wb;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @brief Game board.
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Jashank Jeremy {@literal <z5017851@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 * @date May 2017
 */
class Board
implements Cloneable {
	private static final int undoLength = 5;

	private int width;
	private int height;
	private Tile[][] positions;
	private List<Player> players;
	private List<Crate> crates;
	private List<FloorTile> finishTiles;
	private List<GamePiece> pieces;

	public Board(int width, int height) {
		// Initialise all local variables
		this.width = width;
		this.height = height;
		this.positions = new Tile[width][height];
		this.players = new ArrayList<Player>();
		this.crates = new ArrayList<Crate>();
		this.finishTiles = new ArrayList<FloorTile>();
		this.pieces = new ArrayList<GamePiece>();
	}

	public Tile getPosition(Point pos) {
		if (pos == null) {
			return null;
		}

		int x = pos.x;
		int y = pos.y;

		if ((x < 0 || x >= width) || (y < 0 || y >= height)) {
			return null;
		}

		return this.positions[x][y];
	}

	public void setPosition(Point pos, Tile t) {
		this.positions[pos.x][pos.y] = t;
		GamePiece p = t.getContents();

		/* XXX(jashankj): what's going on here? */
		if (p == null) {
			return;
		}

		/* XXX(jashankj): ????? */
		if (p.getType() == 0) {
			this.players.add((Player) p);
		} else if (p.getType() == 1) {
			this.crates.add((Crate) p);
		}

		this.addPieces();
	}

	public void addFinishTile(FloorTile t) {
		this.finishTiles.add(t);
	}

	public int getHeight() {
		return this.height;
	}

	public int getWidth() {
		return this.width;
	}

	public boolean doMove(int direction) {
		// XXX can only use player 0 for now
		return this.players.get(0).doMove(direction);
	}

	public Tile[][] getTiles() {
		return this.positions;
	}

	public Iterator<Tile> tileIterator() {
		List<Tile> flatten = new ArrayList<Tile>();
		for (Tile[] array : this.positions) {
			flatten.addAll(Arrays.asList(array));
		}
		return flatten.iterator();
	}

	private void addPieces() {
		// this should only be called if an update is made in the num
		// of pieces.
		this.pieces = new ArrayList<GamePiece>();
		this.pieces.addAll(this.crates);
		this.pieces.addAll(this.players);
	}

	public List<GamePiece> gamePieceIterator() {
		// simple reference to all pieces
		return this.pieces;
	}

	public List<Player> getPlayers() {
		return this.players;
	}

	public List<Crate> getCrates() {
		return this.crates;
	}

	public List<FloorTile> getFinishTiles() {
		return this.finishTiles;
	}

	public int getUndoLength() {
		return this.undoLength;
	}

	public void undo() {
		for (GamePiece p : this.pieces) {
			this.getPosition(p.getCoord()).setContents(null);
			p.undo();
			this.getPosition(p.getCoord()).setContents(p);
		}
	}

	public void addPiecesUndo() {
		for (GamePiece p : this.pieces) {
			p.storePrevCoord();
		}
	}

	// Point in direction
	public Point nearbyPoint(Point start, Direction d) {
		int startx = start.x;
		int starty = start.y;

		switch (d) {
		case UP:
			starty--;
			break;

		case DOWN:
			starty++;
			break;

		case LEFT:
			startx--;
			break;

		case RIGHT:
			startx++;
			break;
		}

		if (! (0 < startx && startx <= this.width)) {
			return null;
		}
		if (! (0 < starty && starty <= this.height)) {
			return null;
		}

		Point f = new Point();
		f.setLocation(startx, starty);
		return f;
	}

	/* XXX(jashankj): kill? */
	public void debug(int player, int num) {
		Player p = this.players.get(player);
		// p.getDirection()
		Point coord = this.nearbyPoint(p.getCoord(), Direction.UP);
		Tile toRemove = getPosition(coord);
		if (toRemove == null) {
			return;
		}

		GamePiece contents = toRemove.getContents();
		if (this.finishTiles.contains(toRemove)) {
			this.finishTiles.remove(toRemove);
		}

		if (contents != null) {
			if (this.players.contains(contents)) {
				this.players.remove(contents);
			}
			if (this.crates.contains(contents)) {
				this.crates.remove(contents);
			}
		}

		this.setPosition(coord, FileIO.int2Tile(this, num, coord));
	}

	public boolean isFinished() {
		for (Tile t : this.finishTiles)
			if (t.getContents() == null ||
				t.getContents().getType() == 0)
				return false;

		for (GamePiece p : this.players) {
			Point2D curr = p.getAnimOffset();
			if (curr.getX() != 0 || curr.getY() != 0)
				return false;
		}

		return true;
	}

	@Override
	public String toString() {
		String board = "";
		/* XXX(jashankj): this should be an iterative StringBuilder thing */
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				Tile t = this.positions[j][i];

				if (! t.canBeFilled()) {
					board += "| W |";
				} else if (this.finishTiles.contains(t)) {
					if (t.getContents() == null) {
						board += "| G |";
					} else if (t.getContents().getType() == 0) {
						board += "|PG |";
					} else {
						board += "|CG |";
					}
				} else if (t.getContents() == null) {
					board += "|	  |";
				} else if (t.getContents().getType() == 0) {
					board += "| P |";
				} else {
					board += "| C |";
				}
			}
			board += "\n";
		}
		return board;
	}

	@Override
	public Board clone() {
		Board clone = new Board(this.width, this.height);

		Iterator<Tile> ti = this.tileIterator();
		while (ti.hasNext()) {
			Tile basedOff = ti.next();
			Tile toAdd = null;

			if (basedOff.canBeFilled()) {
				toAdd = new FloorTile(basedOff.getCoord());
			} else {
				toAdd = new Wall(basedOff.getCoord());
			}

			Point addAt = toAdd.getCoord();
			if (basedOff.getContents() != null) {
				if (basedOff.getContents().getType() == 1) {
					toAdd.setContents(new Crate(clone, addAt));
				} else {
					toAdd.setContents(new Player(clone, addAt));
				}
			}

			clone.setPosition(addAt, toAdd);
		}
		return clone;
	}
}
