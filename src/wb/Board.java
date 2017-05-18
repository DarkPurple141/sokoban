package wb;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class Board {
	private int width;
	private int height;
	private int undoLength = 5;
	private Tile[][] positions;

	private List<Player> players;
	private List<Crate> crates;
	private List<FloorTile> finishTiles;

	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.positions = new Tile[width][height];
		//Initialise all local variables
		players = new ArrayList<>();
		finishTiles = new ArrayList<>();
		crates = new ArrayList<>();
	}
	// TODO pretty sure this is a cause for concern unless
	// u guys have functions that are handling null returns properly.

	public Tile getPosition(Point pos) {
		if(pos == null)
			return null;
		int x = pos.x;
		int y = pos.y;
		if (x < 0 || x >= width){
			return null;
		}
		if (y < 0 || y >= height){
			return null;
		}
		return positions[x][y];
	}

	public void setPosition(Point pos, Tile t) {
		this.positions[pos.x][pos.y] = t;
		GamePiece p = t.getContents();
		if(p != null) {
			if (p.getType() == 0)
				players.add((Player) p);
			else if (p.getType() == 1)
				crates.add((Crate) p);
		}
	}

	public void addFinishTile(FloorTile t) {
		finishTiles.add(t);
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public boolean doMove(int direction){
		return players.get(0).doMove(direction);//Can only use player 0 for now
	}

	public Tile[][] getTiles() {
		return positions;
	}

	public Iterator<Tile> tileIterator() {
		List<Tile> flatten = new ArrayList<>();
		for(Tile[] array : positions) {
			flatten.addAll(Arrays.asList(array));
		}
		return flatten.iterator();
	}

	public Iterator<GamePiece> gamePieceIterator() {
		List<GamePiece> l = new ArrayList<>();
		l.addAll(crates);
		l.addAll(players);
		return l.iterator();
	}

	public List<Player> getPlayers() {
		return players;
	}

	public List<Crate> getCrates() {
		return crates;
	}

	public List<FloorTile> getFinishTiles() {
		return finishTiles;
	}

	public int getUndoLength() {
		return undoLength;
	}

	public void undo() {
		for (Player p : players){
			getPosition(p.getCoord()).setContents(null);
			p.undo();
			getPosition(p.getCoord()).setContents(p);
		}
		for (Crate c : crates){
			getPosition(c.getCoord()).setContents(null);
			c.undo();
			getPosition(c.getCoord()).setContents(c);
		}
	}

	public void addPiecesUndo(){
		for (Player p : players){
			p.storePrevCoord();
		}

		for (Crate c : crates){
			c.storePrevCoord();
		}
	}

	public void debug(int player, int num) {
		Player p = players.get(player);
		Point coord = p.nearbyPoint(p.getDirection());
		Tile toRemove = getPosition(coord);
		if(toRemove == null)
			return;
		GamePiece contents = toRemove.getContents();
		if(finishTiles.contains(toRemove))
			finishTiles.remove(toRemove);
		if(contents != null) {
			if(players.contains(contents))
				players.remove(contents);
			if(crates.contains(contents))
				crates.remove(contents);
		}
		setPosition(coord, FileIO.int2Tile(this, num, coord));
	}

	public boolean isFinished() {
		for (Tile t : finishTiles){
			if (t.getContents() == null || t.getContents().getType() == 0){
				return false;
			}
		}

		for (GamePiece p : players) {
			Point2D curr = p.getAnimOffset();
			if (curr.getX() != 0 || curr.getY() != 0) {
				return false;
			}
		}

		return true;
	}
}
