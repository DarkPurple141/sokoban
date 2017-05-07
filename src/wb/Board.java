package wb;

class Board {

	Tile[][] positions;
    
    public Board(int[][] tileArray){
    	System.out.println("---------");
		for (int[] row : tileArray){
			for(int col : row){
				System.out.print("|" + col + "|");
			}
			System.out.println();
			System.out.println("---------");
		}
    }

    public Tile getPosition(Coord pos)
	{
		int x = pos.getx();
		int y = pos.gety();
		return positions[x][y];
	}

}
