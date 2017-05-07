package wb;

class Tile {

	Coord tileCoord;
	public Tile(Coord tileCoord){
		this.tileCoord = tileCoord;
	}
    public Coord getCoord(){
    	return tileCoord;
    }

    public boolean canBeFilled(){
        return false;
    }
}
