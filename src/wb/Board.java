package wb;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class Board implements Iterable<Tile> {
	private Tile[][] positions;
	private List<FloorTile> finishTiles;
	private List<Crate> crates;

	/**
	 * Constructor
	 *
	 * Given the 2D array of board setup, initialises the list of Tiles
	 * with their coords/contents set correctly.
	 *
	 *
	 * @param tileArray : The 2D array representation of a board with the numbers
	 *                  representing what object is on the Tile at the corresponding coordinate
	 *
	 */

	public Board(int[][] tileArray, Player p) {
		positions = new Tile[tileArray.length][tileArray.length];
		finishTiles = new ArrayList<FloorTile>();
		crates = new ArrayList<Crate>();
		int currentY = 0;
		for (int[] row : tileArray) {
			int currentX = 0;
			for (int col : row) {
				Point tileCoord = new Point(currentX, currentY);
				if (col == 1) {
					Tile toAdd = new Wall(tileCoord);
					positions[currentY][currentX] = toAdd;
				} else if (col == 4) {
					FloorTile toAdd = new FloorTile(tileCoord);
					finishTiles.add(toAdd);
					positions[currentY][currentX] = toAdd;
				} else {
					FloorTile toAdd = new FloorTile(tileCoord);
					switch (col) {
						case 2:
							p.setCoord(tileCoord);
							toAdd.setContents(p);
							break;
						case 3:
							Crate crate = new Crate(tileCoord);
							toAdd.setContents(crate);
							crates.add(crate);
							break;
					}
					positions[currentY][currentX] = toAdd;
				}
				currentX++;
			}
			currentY++;
		}

	}

	// TODO pretty sure this is a cause for concern unless
	// u guys have functions that are handling null returns properly.
	public Tile getPosition(Point pos) {
		if(pos == null)
			return null;
		int x = pos.x;
		int y = pos.y;
		if (x < 0 || x >= positions.length){
			return null;
		}
		if (y < 0 || y >= positions.length){
			return null;
		}
		return positions[y][x];
	}

	public int getHeight() {
		return positions.length;
	}

	public int getWidth() {
		//return positions.length();
		return positions.length;
	}

	public Tile[][] getTiles(){
		return positions;
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
}
