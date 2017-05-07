package wb;

/**
 * Created by Ben on 2/5/17.
 */
public class FinishTile extends ContainerTile
{
	public FinishTile(Coord tileCoord){
		super(tileCoord);
	}
    public boolean hasBox(){
    	return false;
    }
}
