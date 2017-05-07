package wb;

/**
 * Created by Ben on 2/5/17.
 */
public class ContainerTile extends Tile
{

	Object contents;
	public ContainerTile(Coord tileCoord){
		super(tileCoord);
		contents = null;
	}
    @Override
    public boolean canBeFilled(){
        return true;
    }

    public Object getContents(){
    	return contents;
    }

    public void setContents(Object newContents){
    	contents = newContents;
    }
}
