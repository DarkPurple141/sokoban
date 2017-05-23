package wb;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class Board implements Cloneable{
	private int width;
	private int height;
	private int undoLength = 5;
	private Tile[][] positions;

	private List<Player> players;
	private List<Crate> crates;
	private List<FloorTile> finishTiles;
	private List<GamePiece> pieces;

	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.positions = new Tile[width][height];
		//Initialise all local variables
		players = new ArrayList<>();
		pieces = new ArrayList<>();
		finishTiles = new ArrayList<>();
		crates = new ArrayList<>();
	}

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
			else if (p.getType() == 1){
				crates.add((Crate) p);
			}
			addPieces();
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

	public void addPieces() {
		// this should only be called if an update is made in the num of pieces.
		pieces = new ArrayList<GamePiece>();
		pieces.addAll(crates);
		pieces.addAll(players);
	}

	public List<GamePiece> gamePieceIterator() {
		// simple reference to all pieces
		return pieces;
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

		// Needs to undo players first
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
		for (GamePiece p : pieces){
			p.storePrevCoord();
		}
	}

	public Point nearbyPoint(Point start, int direction) {
		int startx = start.x;
		int starty = start.y;
		if(direction == 0)
			starty--;
		else if(direction == 1)
			startx++;
		else if(direction == 2)
			starty++;
		else if(direction == 3)
			startx--;
		if(startx < 0 || width <= startx)
			return null;
		if(starty < 0 || height <= starty)
			return null;
		Point f = new Point();
		f.setLocation(startx, starty);
		return f;
	}

	public void debug(int player, int num) {
		Player p = players.get(player);
		Point coord = nearbyPoint(p.getCoord(), p.getDirection());
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

	public String toString(){
		String board = "";
		for (int i = 0; i < height; i++){
			for (int j = 0; j < width; j++){
				if (!positions[j][i].canBeFilled()){
					board += "| W |";
				}else if(finishTiles.contains(positions[j][i])){
					if (positions[j][i].getContents() == null){
						board += "| G |";
					}else if (positions[j][i].getContents().getType() == 0){
						board += "|PG |";
					}else{
						board += "|CG |";
					}
				}else if(positions[j][i].getContents() == null){
					board += "|   |";
				}else if(positions[j][i].getContents().getType() == 0){
					board += "| P |";
				}else{
					board += "| C |";
				}
			}
			board += "\n";
		}
		return board;
	}

	public Board clone(){

		Board clone = new Board(width, height);
		Iterator<Tile> ti = tileIterator();
		while(ti.hasNext()){
			Tile basedOff = ti.next();
			Tile toAdd = null;
			if (basedOff.canBeFilled()){
				toAdd = new FloorTile(basedOff.getCoord());
			}else{
				toAdd = new Wall(basedOff.getCoord());
			}
			
			Point addAt = toAdd.getCoord();
			if (basedOff.getContents() != null){
				if (basedOff.getContents().getType() == 1){
					toAdd.setContents(new Crate(clone, addAt));
				}else{
					toAdd.setContents(new Player(clone, addAt));
				}
			}
			clone.setPosition(addAt, toAdd);
		}

		for (FloorTile f : finishTiles){
			clone.addFinishTile((FloorTile)clone.getPosition(f.getCoord()));
		}

		return clone;
	}


}
