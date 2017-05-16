package wb;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class Board implements Iterable<Tile> {
	private int width;
	private int height;
	private Tile[][] positions;
	private List<Player> players;
	private List<Crate> crates;
	private List<FloorTile> finishTiles;

	public Board(String filePath) {
		//Initialise all local variables
		players = new ArrayList<>();
		finishTiles = new ArrayList<>();
		crates = new ArrayList<>();

		this.xmlToLevel(filePath);
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

	public void saveGame(String filename){
		DocumentBuilderFactory documentBuilderF = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder builder = documentBuilderF.newDocumentBuilder();
			Document saveFile = builder.newDocument();
			Element board = saveFile.createElement("board");
			Attr saveWidth = saveFile.createAttribute("width");
			saveWidth.setValue(Integer.toString(width));
			board.setAttributeNode(saveWidth);
			Attr saveHeight = saveFile.createAttribute("height");
			saveHeight.setValue(Integer.toString(height));
			board.setAttributeNode(saveHeight);
			saveFile.appendChild(board);
			for (Tile[] rowTiles : positions){
				Element row = saveFile.createElement("col");
				board.appendChild(row);
				for (Tile colTile : rowTiles){
					Element col = saveFile.createElement("row");
					if (!colTile.canBeFilled()){
						col.appendChild(saveFile.createTextNode("1"));
					}else if(finishTiles.contains(colTile)){
						if (colTile.getContents() == null){
							col.appendChild(saveFile.createTextNode("4"));
						}else if(colTile.getContents().getType() == 0){
							col.appendChild(saveFile.createTextNode("6"));
						}else{
							col.appendChild(saveFile.createTextNode("5"));
						}
					}else if(colTile.getContents() == null){
						col.appendChild(saveFile.createTextNode("0"));
					}else if(colTile.getContents().getType() == 1){
						col.appendChild(saveFile.createTextNode("3"));
					}else{
						col.appendChild(saveFile.createTextNode("2"));
					}
					row.appendChild(col);
				}
			}

			TransformerFactory transformerF = TransformerFactory.newInstance();
			Transformer transformer = transformerF.newTransformer();

			DOMSource save = new DOMSource(saveFile);
			StreamResult result = new StreamResult(new File(filename + ".xml"));
			//StreamResult result = new StreamResult(System.out);
			transformer.transform(save, result);


		}catch(Exception e){
			System.out.println(e.getMessage());
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
		positions[coord.x][coord.y] = intToTile(num, coord.x, coord.y);
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
	private void xmlToLevel(String filePath) {

		DocumentBuilderFactory documentBuilderF = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder builder = documentBuilderF.newDocumentBuilder();
			File levelFile = new File(filePath);
			Document levelDoc = builder.parse(levelFile);
			Element board = levelDoc.getDocumentElement();
			width = Integer.parseInt(board.getAttribute("width"));
			height = Integer.parseInt(board.getAttribute("height"));
			positions = new Tile[width][height];
			NodeList cols = levelDoc.getElementsByTagName("col");
			for(int x = 0; x < cols.getLength(); x++) {
				Element col = (Element)cols.item(x);
				NodeList rows = col.getElementsByTagName("row");
				for(int y = 0; y < rows.getLength(); y++){
					positions[x][y] = intToTile(Integer.parseInt(rows.item(y).getTextContent()), x, y);
				}

			}
		} catch(Exception e) {
			System.out.println(e.getMessage());

		}
	}

	private Tile intToTile(int code, int x, int y) {
		Point pos = new Point();
		pos.x = x;
		pos.y = y;
		if(code == 0){
			return new FloorTile(pos);
		}
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
		if(code == 5) {
			FloorTile t = new FloorTile(pos);
			finishTiles.add(t);
			Crate c = new Crate(this, pos);
			crates.add(c);
			t.setContents(c);
			return t;
		}
		if(code == 6) {
			FloorTile t = new FloorTile(pos);
			finishTiles.add(t);
			Player p = new Player(this, pos);
			players.add(p);
			t.setContents(p);
			return t;
		}
		return null;
	}

	public boolean isFinished() {
		for (Tile t : finishTiles){
			if (t.getContents() == null || t.getContents().getType() == 0){
				return false;
			}
		}

		for (GamePiece p : players) {
			Point2D curr = p.getAnimOffset();
			if (curr.getX() != 0.0 && curr.getY() != 0.0) {
				return false;
			}
		}

		return true;
	}
}
