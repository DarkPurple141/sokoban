package wb;
import java.util.ArrayList;
import java.util.List;

// maybe this should be an abstract class?
class Board {
    private Tile[][] tiles;
    private List<Tile> finishTiles;

    /**
     * Constructor
     *
     * Given the 2D array of board setup, initialises the list of Tiles
     * with their coords/contents set correctly.
     *
     * 
     * @param tileArray : The 2D array representation of a board with the numbers
     * representing what object is on the Tile at the corresponding coordinate	
     *
     */
    public Board(int[][] tileArray){
    	tiles = new Tile[tileArray.length][tileArray.length];
    	finishTiles = new ArrayList<Tile>();
    	int currentY = 0;
		for (int[] row : tileArray){
			int currentX = 0;
			Coord tileCoord = new Coord(currentX, currentY);
			for(int col : row){
				if (col == 1){
					Tile toAdd = new Tile(tileCoord);
					tiles[currentY][currentX] = toAdd;
				}else if(col == 4){
					FinishTile toAdd = new FinishTile(tileCoord);
					finishTiles.add(toAdd);
					tiles[currentY][currentX] = toAdd;
				}else{
					ContainerTile toAdd = new ContainerTile(tileCoord);
					switch(col){
						case 2:
							Player player = new Player(tileCoord);
							toAdd.setContents(player);
							break;
						case 3:
							Box box = new Box(tileCoord);
							toAdd.setContents(box);
							break;
					}
					tiles[currentY][currentX] = toAdd;
				}
				currentX++;
			}
			currentY++;
		}

    }

    public Tile getTile(Coord coord){
    	return tiles[coord.getX()][coord.getY()];
    }

    public List<Tile> getFinishTiles(){
    	return finishTiles;
    }

}
