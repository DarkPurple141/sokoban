package wb;

// maybe this should be an abstract class?
class Board {
    
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

}
