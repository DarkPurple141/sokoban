package wb;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class Board implements Iterable<Tile> {
	int width;
	int height;
	private Tile[][] positions;
	private List<Player> players;
	private List<Crate> crates;
	private List<FloorTile> finishTiles;

	public Board(int width, int height, String filePath) {
		//Initialise all local variables
		this.width = width;
		this.height = height;
		positions = new Tile[width][height];//To be moved to XML2Level
		players = new ArrayList<>();
		finishTiles = new ArrayList<>();
		crates = new ArrayList<>();


		XML2Level(filePath);

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
		return positions[y][x];
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

	public Tile[][] getTiles(){
		return positions;
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

	@Override
	public Iterator<Tile> iterator() {
		List<Tile> flatten = new ArrayList<>();
		for(Tile[] array : positions) {
			flatten.addAll(Arrays.asList(array));
		}
		return flatten.iterator();
	}

	public boolean isFinishTile(Tile toCheck){
		return finishTiles.contains(toCheck);
	}

	/**
	 * Used when constructing a Model from an xml file
	 * There is potential to use the same XML format for saved games as well
	 * Just need to include 2 more possible states:
	 * 		1. Finish Tile w Player
	 * 		2. Finsh Tile w Box
	 *
	 * @return The 2D array of the board with columns as the secondary layer
	 * Format:
	 * 		[row][col,col,col...]
	 * 		[row][col,col,col...]
	 * 		.
	 * 		.
	 * 		.
	 * 		[row][col,col,col...]
	 * @param filePath : The path to the XML file being used to create the model (relative from base dirctory (cs2911-proj1))
	 * See "level1.xml" for XML formatting
	 *
	 */
	private void XML2Level(String filePath) {
		DocumentBuilderFactory documentBuilderF = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = documentBuilderF.newDocumentBuilder();
			File levelFile = new File(filePath);

			Document levelDoc = builder.parse(levelFile);
			Element board = levelDoc.getDocumentElement();
			System.out.println(board);
			NodeList rows = levelDoc.getElementsByTagName("row");
			for(int y = 0; y < rows.getLength(); y++) {
				Element row = (Element)rows.item(y);
				NodeList cols = row.getElementsByTagName("col");
				for(int x = 0; x < cols.getLength(); x++)
					positions[x][y] = int2Tile(Integer.parseInt(cols.item(x).getTextContent()), x, y);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private Tile int2Tile(int code, int x, int y) {
		Point pos = new Point();
		pos.x = x;
		pos.y = y;
		if(code == 1) {
			return new Wall(pos);
		}
		if(code == 2) {
			Tile t = new FloorTile(pos);
			Player p = new Player(this, pos);
			players.add(p);
			t.setContents(p);
			return t;
		}
		if(code == 3) {
			FloorTile t = new FloorTile(pos);
			Crate c = new Crate(this, pos);
			crates.add(c);
			t.setContents(c);
			return t;
		}
		if(code == 4) {
			FloorTile t = new FloorTile(pos);
			finishTiles.add(t);
			return t;
		}
		return null;
	}
}
