package wb;

import javax.swing.*;

public class FinishTile extends ContainerTile
{
    public FinishTile(Coord startCoord)
    {
        super(startCoord);
    }

    public boolean hasBox(){
    	return super.getContents() instanceof Box;
    }
}
