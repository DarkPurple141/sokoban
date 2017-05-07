package wb;

public class ContainerTile extends Tile
{

    private Object contents;

    public ContainerTile(Coord startCoord)
    {
        super(startCoord);
        this.contents = null;
    }

    @Override
    public boolean canBeFilled(){
        return true;
    }

    public void setContents(Object content) {
        contents = content;
    }

    public Object getContents(){
    	return contents;
    }
}
