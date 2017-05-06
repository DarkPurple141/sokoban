package wb;

/**
 * Created by Ben on 2/5/17.
 */
public class ContainerTile extends Tile
{
    @Override
    public boolean canBeFilled(){
        return true;
    }

    public Object getContents(){
    	return null;
    }
}
