package wb;
import java.util.ArrayList;
import java.util.List;

class Board {
    private Tile[][] positions;
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

    
    public Board(int[][] tileArray, Player p){
    	positions = new Tile[tileArray.length][tileArray.length];
    	finishTiles = new ArrayList<Tile>();
    	int currentY = 0;
		for (int[] row : tileArray){
			int currentX = 0;
			Coord tileCoord = new Coord(currentX, currentY);
			for(int col : row){
				if (col == 1){
					Tile toAdd = new Tile(tileCoord);
					positions[currentY][currentX] = toAdd;
				}else if(col == 4){
					FinishTile toAdd = new FinishTile(tileCoord);
					finishTiles.add(toAdd);
					positions[currentY][currentX] = toAdd;
				}else{
					ContainerTile toAdd = new ContainerTile(tileCoord);
					switch(col){
						case 2:
							p.setCoord(tileCoord);
							toAdd.setContents(p);
							break;
						case 3:
							Crate crate = new Crate(tileCoord);
							toAdd.setContents(crate);
							break;
					}
					positions[currentY][currentX] = toAdd;
				}
				currentX++;
			}
			currentY++;
		}

    }

    public List<Tile> getFinishTiles(){
    	return finishTiles;
    }

    public Tile getPosition(Coord pos)
	{
		int x = pos.getX();
		int y = pos.getY();
		return positions[x][y];
	}

}