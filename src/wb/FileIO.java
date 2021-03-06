package wb;

import java.awt.Point;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * serialisation and deserialisation for Board
 *
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
class FileIO {
	/**
	 * Used when constructing a Model from an xml file
	 * There is potential to use the same XML format for saved games as well
	 * Just need to include 2 more possible states:
	 *		1. Finish Tile w Player
	 *		2. Finsh Tile w Box
	 *
	 * @return The 2D array of the board with columns as the secondary layer
	 * Format:
	 *		[row][col,col,col...]
	 *		[row][col,col,col...]
	 *		.
	 *		.
	 *		.
	 *		[row][col,col,col...]
	 * @param filePath : The path to the XML file being used to create the model (relative from base dirctory (cs2911-proj1))
	 * See "level1.xml" for XML formatting
	 *
	 */
	public static Board XML2Board(String filePath) {
		int width = 0;
		int height = 0;
		Board b = null;

		DocumentBuilderFactory documentBuilderF = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder builder = documentBuilderF.newDocumentBuilder();
			File levelFile = new File(filePath);
			Document levelDoc = builder.parse(levelFile);
			Element board = levelDoc.getDocumentElement();
			width = Integer.parseInt(board.getAttribute("width"));
			height = Integer.parseInt(board.getAttribute("height"));

			b = new Board(width, height);

			NodeList cols = levelDoc.getElementsByTagName("col");
			for(int x = 0; x < cols.getLength(); x++) {
				Element col = (Element)cols.item(x);
				NodeList rows = col.getElementsByTagName("row");
				for(int y = 0; y < rows.getLength(); y++){
					Point pos = new Point();
					pos.setLocation(x, y);
					int code = Integer.parseInt(rows.item(y).getTextContent());
					b.setPosition(pos, int2Tile(b, code, pos));
				}
			}
		} catch (Exception e) {
		}
		return b;
	}

	/**
	 * Uses the built-in XML code to convert an integer to a tile and place it on the board
	 * @param b the board to place on
	 * @param code the code of the tile to create
	 * @param pos the position to place the tile
	 * @return The tile that was placed
	 */
	public static Tile int2Tile (Board b, int code, Point pos) {
		Tile t;
		FloorTile ft;
		GamePiece content;

		switch (code) {
		case 1:
			t = new WallTile (pos);
			break;

		case 0:
		case 2: case 6:
		case 3: case 5:
		case 4:
			ft = new FloorTile (pos);
			t = (Tile)ft;

			if (code == 2 || code == 6) {
				ft.setContents (new Player (b, pos));
			}

			if (code == 3 || code == 5) {
				ft.setContents (new Crate (b, pos));
			}

			if (! (4 <= code && code <= 6)) {
				break;
			}

			b.addFinishTile(ft);
			break;

		default:
			throw new IllegalStateException ();
		}

		return t;
	}

	/**
	 * Parses the board back into XML format and saves it as a file
	 * @param b the board to save
	 * @param filename the file path to save as
	 */
	public static void saveGame(Board b, String filename){
		DocumentBuilderFactory documentBuilderF = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder builder = documentBuilderF.newDocumentBuilder();
			Document saveFile = builder.newDocument();
			Element board = saveFile.createElement("board");
			Attr saveWidth = saveFile.createAttribute("width");
			saveWidth.setValue(Integer.toString(b.getWidth()));
			board.setAttributeNode(saveWidth);
			Attr saveHeight = saveFile.createAttribute("height");
			saveHeight.setValue(Integer.toString(b.getHeight()));
			board.setAttributeNode(saveHeight);
			saveFile.appendChild(board);
			for (Tile[] rowTiles : b.getTiles()){
				Element row = saveFile.createElement("col");
				board.appendChild(row);
				for (Tile colTile : rowTiles){
					Element col = saveFile.createElement("row");
					if (!colTile.canBeFilled()){
						col.appendChild(saveFile.createTextNode("1"));
					}else if(b.getFinishTiles().contains(colTile)){
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
			StreamResult result = new StreamResult(new File(filename));
			//StreamResult result = new StreamResult(System.out);
			transformer.transform(save, result);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes a file at the given path
	 * @param filename the path to delete
	 */
	public static void removeFile(String filename){
		try {
			File f = new File(filename);
			f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clears the entire levels folder to make room for a new round of generation
	 */
	public static void deleteAllLevels(){
		File[] oldLevels = new File("levels/").listFiles();
		for (File f : oldLevels){
			removeFile("levels/" + f.getName());
		}
	}

	/**
	 * Populates settings from the settings file
	 * @param toFill the data structure to populate
	 */
	public static void fillSettings(Settings toFill){
		DocumentBuilderFactory documentBuilderF = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = documentBuilderF.newDocumentBuilder();
			File settingsFile = new File("settings");
			Document settingsDoc = builder.parse(settingsFile);
			Element allSettings = settingsDoc.getDocumentElement();
			Node difficultyNode = allSettings.getElementsByTagName("difficulty").item(0);

			switch(difficultyNode.getTextContent()){
			case "0":
				toFill.setDifficulty(Difficulty.EASY);
				break;
			case "1":
				toFill.setDifficulty(Difficulty.MEDIUM);
				break;
			case "2":
				toFill.setDifficulty(Difficulty.HARD);
				break;
			}

			Node player = allSettings.getElementsByTagName("playername").item(0);
			toFill.setPlayerName(player.getTextContent());

			Node gameSpeed = allSettings.getElementsByTagName("speed").item(0);
			toFill.setMoveIncrement(Double.parseDouble(gameSpeed.getTextContent()));

		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Saves settings to the settings file
	 * @param toSave the data structure to save from
	 */
	public static void saveSettings(Settings toSave){
		DocumentBuilderFactory documentBuilderF = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = documentBuilderF.newDocumentBuilder();
			Document settingsFile = builder.newDocument();
			Element allSettings = settingsFile.createElement("settings");
			settingsFile.appendChild(allSettings);
			Element difficultyNode = settingsFile.createElement("difficulty");
			String difficultyEncoding = "";
			switch (toSave.getDifficulty()){
			case EASY:
				difficultyEncoding = "0";
				break;
			case HARD:
				difficultyEncoding = "2";
				break;
			default:
				difficultyEncoding = "1";
				break;
			}
			difficultyNode.appendChild(settingsFile.createTextNode(difficultyEncoding));
			allSettings.appendChild(difficultyNode);

			Element playerName = settingsFile.createElement("playername");
			String name = toSave.getPlayerName();
			
			playerName.appendChild(settingsFile.createTextNode(toSave.getPlayerName()));
			allSettings.appendChild(playerName);

			Element gameSpeed = settingsFile.createElement("speed");
			gameSpeed.appendChild(settingsFile.createTextNode(Double.toString(toSave.getMoveIncrement())));
			allSettings.appendChild(gameSpeed);

			TransformerFactory transformerF = TransformerFactory.newInstance();
			Transformer transformer = transformerF.newTransformer();

			DOMSource savedSettings = new DOMSource(settingsFile);
			StreamResult result = new StreamResult(new File("settings"));
			//StreamResult result = new StreamResult(System.out);
			transformer.transform(savedSettings, result);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
