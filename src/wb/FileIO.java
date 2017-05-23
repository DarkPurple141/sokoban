package wb;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;

/**
 * @brief Serialisation and deserialisation.
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 * @date May 2017
 */
public class FileIO {
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
		} catch(Exception e) {
			System.out.println(e.getMessage());

		}
		return b;
	}

	public static Tile int2Tile(Board b, int code, Point pos) {
		if(code == 0){
			return new FloorTile(pos);
		}
		if(code == 1) {
			return new Wall(pos);
		}
		if(code == 2) {
			Tile t = new FloorTile(pos);
			t.setContents(new Player(b, pos));
			return t;
		}
		if(code == 3) {
			FloorTile t = new FloorTile(pos);
			t.setContents(new Crate(b, pos));
			return t;
		}
		if(code == 4) {
			FloorTile t = new FloorTile(pos);
			b.addFinishTile(t);
			return t;
		}
		if(code == 5) {
			FloorTile t = new FloorTile(pos);
			t.setContents(new Crate(b, pos));
			b.addFinishTile(t);
			return t;
		}
		if(code == 6) {
			FloorTile t = new FloorTile(pos);
			t.setContents(new Player(b, pos));
			b.addFinishTile(t);
			return t;
		}
		return null;
	}

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
			StreamResult result = new StreamResult(new File(filename + ".xml"));
			//StreamResult result = new StreamResult(System.out);
			transformer.transform(save, result);


		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
