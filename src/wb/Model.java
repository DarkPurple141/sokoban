package wb;
import java.util.List;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

class Model {

    private Board board;
    //private Player p;
    

    public Model(){
    	// constructor from randomly generated
    }

    /**
     * Constructor.
     *
     * Makes a new model from an XML file 'filename'
     * Used to create fixed levels
     *
     */
    public Model(String filePath){
    	int[][] boardArray = parseXML(filePath);
    	board = new Board(boardArray);

    }

    /**
     * Used when constructing a Model from an xml file
     * There is potential to use the same XML format for saved games as well
     * Just need to include 2 more possible states:
     * 		1. Finish Tile w Player
     * 		2. Finsh Tile w Boxx
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
    private int[][] parseXML(String filePath){
    	DocumentBuilderFactory documentBuilderF = DocumentBuilderFactory.newInstance();
    	int[][] boardArray = null;
    	try {
	    	DocumentBuilder builder = documentBuilderF.newDocumentBuilder();
	    	File levelFile = new File(filePath);
	    	try {
	    		Document levelDoc = builder.parse(levelFile);
	    		NodeList rows = levelDoc.getElementsByTagName("row");
	    		boardArray = new int[rows.getLength()][rows.getLength()];
	    		for (int i = 0; i < rows.getLength(); i++){
	    			Element row = (Element)rows.item(i);
	    			NodeList cols = row.getElementsByTagName("col");
	    			boardArray[i] = new int[cols.getLength()];
	    			for (int j = 0; j < cols.getLength(); j++){
	    				boardArray[i][j] = Integer.parseInt(cols.item(j).getTextContent());
	    			}
	    		}
	    	} catch (Exception e){
	    		System.out.println(e);
	    	}
    	}catch (Exception e){
    		System.out.println(e);
    	}finally{
    		return boardArray;
    	}
    	
    }

}
