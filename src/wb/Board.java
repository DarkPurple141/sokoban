package wb;

import org.omg.PortableInterceptor.DISCARDING;

import java.awt.Point;
import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Game board.
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Jashank Jeremy {@literal <z5017851@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
class Board
implements Cloneable {
	public static final int UNDO_LENGTH = 5;

	private int width;
	private int height;
	private Tile[][] positions;
	private List<Player> players;
	private List<Crate> crates;
	private List<FloorTile> finishTiles;
	private List<GamePiece> pieces;

	/**
	 * Initialises the board with a given width and height
	 * @param width the width in tiles
	 * @param height the height in tiles
	 */
	public Board(int width, int height) {
		//Initialise all local variables
		this.width = width;
		this.height = height;
		this.positions = new Tile[width][height];
		this.players = new ArrayList<Player>();
		this.crates = new ArrayList<Crate>();
		this.finishTiles = new ArrayList<FloorTile>();
		this.pieces = new ArrayList<GamePiece>();
	}

	/**
	 * Returns the Tile at a given position
	 * @param pos the position of the tile
	 * @return the tile at the position
	 */
	public Tile getPosition(Point pos) {
		if (pos == null) {
			return null;
		}

		if (pos.x < 0 || pos.x >= this.width ||
			pos.y < 0 || pos.y >= this.height) {
			return null;
		}

		return this.positions[pos.x][pos.y];
	}

	/**
	 * Overwrites the tile at a given position
	 * Updates the lists of gamepieces with anything in that tile
	 * @param pos the position to set the tile
	 * @param t the new tile to use
	 */
	public void setPosition(Point pos, Tile t) {
		if (pos == null) {
			throw new IllegalArgumentException();
		}

		this.positions[pos.x][pos.y] = t;

		GamePiece gp;
		if ((gp = t.getContents()) == null) {
			return;
		}

		if (gp.getType() == 0) {
			this.players.add((Player) gp);
		} else if (gp.getType() == 1) {
			this.crates.add((Crate) gp);
		}
		this.pieces.add(gp);
	}

	/**
	 * Adds a FloorTile to the list of finish tiles
	 * @param t the FloorTile to add
	 */
	public void addFinishTile(FloorTile t) {
		this.finishTiles.add(t);
	}


	/**
	 * Returns the width of the board
	 * @return the width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Returns the height of the board
	 * @return the height
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Tries to make a move by a player
	 * @param dir the direction to move
	 * @return true if the move was successful
	 */
	public boolean doMove(Direction dir) {
		// FIXME(jashankj): factor out the 0
		// Can only use player 0 for now
		return this.players.get(0).doMove(dir);
	}

	/**
	 * Returns the 2d array of tiles on the board
	 * @return the 2d array of tiles
	 */
	public Tile[][] getTiles() {
		return this.positions;
	}

	/**
	 * Returns an iterator one can use to interate through each tile
	 * @return
	 */
	public Iterator<Tile> tileIterator() {
		// FIXME(jashankj): rename this method
		// FIXME(jashankj): fix for efficiency?
		List<Tile> flatten = new ArrayList<Tile>();
		for (Tile[] array : this.positions) {
			flatten.addAll(Arrays.asList(array));
		}
		return flatten.iterator();
	}

	/**
	 * Returns the list of all game pieces on the board
	 * @return List of GamePieces
	 */
	public List<GamePiece> gamePieces() {
		return this.pieces;
	}

	/**
	 * Returns the list of players on the board
	 * @return List of Players
	 */
	public List<Player> getPlayers() {
		return this.players;
	}

	/**
	 * Returns the list of Crates on the board
	 * @return List of Crates
	 */
	public List<Crate> getCrates() {
		return this.crates;
	}

	/**
	 * Returns the list of FloorTiles marked as finish tiles
	 * @return the List of finish tiles
	 */
	public List<FloorTile> getFinishTiles() {
		return this.finishTiles;
	}

	/**
	 * Undoes one step on the board
	 */
	public void undo() {

		// Needs to undo players first
		for (Player p : this.players) {
			getPosition(p.getCoord()).setContents(null);
			p.undo();
			getPosition(p.getCoord()).setContents(p);
		}

		for (Crate c : this.crates) {
			getPosition(c.getCoord()).setContents(null);
			c.undo();
			getPosition(c.getCoord()).setContents(c);
		}
	}

	/**
	 * Adds a new undo waypoint for each piece on the board
	 */
	public void addPiecesUndo() {
		for (GamePiece p : this.pieces) {
			p.storePrevCoord();
		}
	}

	/**
	 * Returns a point adjacent to an arbitrary point using a given direction
	 * @param start the point to use as the origin
	 * @param dir the direction to find the adjacent point in
	 * @return the found adjacent point
	 */
	public Point nearbyPoint(Point start, Direction dir) {
		// FIXME(jashankj): Direction refactoring
		// FIXME(jashankj): Point-in-Direction refactorin
		int startx = start.x;
		int starty = start.y;

		if (dir == Direction.UP)
			starty--;
		else if (dir == Direction.RIGHT)
			startx++;
		else if (dir == Direction.DOWN)
			starty++;
		else if (dir == Direction.LEFT)
			startx--;

		if (startx < 0 || width <= startx)
			return null;
		if (starty < 0 || height <= starty)
			return null;

		Point f = new Point();
		f.setLocation(startx, starty);
		return f;
	}

	/**
	 * Overwrites the tile in front of a player based on the XML code
	 * @param player the player to set the tile in front of
	 * @param num the XML coded id for the tile to set
	 */
	public void debug(int player, int num) {
		Player p = this.players.get(player);
		Point coord = this.nearbyPoint(p.getCoord(), p.getDirection());

		Tile toRemove = this.getPosition(coord);
		if (toRemove == null) {
			return;
		}

		if (this.finishTiles.contains(toRemove)) {
			this.finishTiles.remove(toRemove);
		}

		GamePiece contents = toRemove.getContents();
		if (contents != null) {
			if (this.players.contains(contents)) {
				players.remove(contents);
			}

			if (crates.contains(contents)) {
				crates.remove(contents);
			}
		}

		this.setPosition(coord, FileIO.int2Tile(this, num, coord));
	}

	/**
	 * Checks to see if the level is finished
	 * Each finish tile is checked to see if it is filled by a crate
	 * @return true if all finish tiles contain a crate
	 */
	public boolean isFinished() {
		for (Tile t : this.finishTiles) {
			GamePiece tc = t.getContents();
			if (tc == null || tc.getType() == 0) {
				return false;
			}
		}

		for (GamePiece p : this.players) {
			Point2D curr = p.getAnimOffset();
			if (curr.getX() != 0 || curr.getY() != 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the board as a string
	 * @return visual representation of the board when printed
	 */
	public String toString() {
		// FIXME(jashankj): use a StringBuilder
		String board = "";

		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				board += "|";
				if (! positions[j][i].canBeFilled()) {
					board += " W ";

				} else if (finishTiles.contains(positions[j][i])) {
					if (positions[j][i].getContents() == null) {
						board += " ";
					} else if (positions[j][i].getContents().getType() == 0) {
						board += "P";
					} else {
						board += "C";
					}

					board += "G ";

				} else if (positions[j][i].getContents() == null) {
					board += "   ";
				} else if (positions[j][i].getContents().getType() == 0) {
					board += " P ";
				} else {
					board += " C ";
				}

				board += "|";
			}

			board += "\n";
		}

		return board;
	}

	/**
	 * Returns a copy of the board
	 * @return new Board with identical properties and fields
	 */
	public Board clone() {
		// TODO: DO this properly
		Board clone = new Board(width, height);

		// FIXME(jashankj): can this be done with a foreach?
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
			clone.setPosition(addAt, toAdd);
		}

		for (Player p : this.players) {
			Tile replacement = new FloorTile(p.getCoord());
			replacement.setContents(new Player(clone, p.getCoord()));
			clone.setPosition(p.getCoord(), replacement);
		}

		for (Crate c : this.crates) {
			Tile replacement = new FloorTile(c.getCoord());
			replacement.setContents(new Crate(clone, c.getCoord()));
			clone.setPosition(c.getCoord(), replacement);
		}

		for (FloorTile f : this.finishTiles) {
			clone.addFinishTile((FloorTile)clone.getPosition(f.getCoord()));
		}

		return clone;
	}
}
