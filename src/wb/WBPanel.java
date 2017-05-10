package wb;
import javax.swing.JPanel;
import java.awt.*;
import java.util.Iterator;
import javax.swing.BorderFactory;
import java.awt.Dimension;

public class WBPanel extends JPanel {

    private Board b;

    public WBPanel(Board b) {
        super();
        setBorder(BorderFactory.createLineBorder(Color.black));
        this.b = b;
    }

    private void paintBackground(Graphics g) {
        int panel_width = this.getWidth();
        int panel_height = this.getHeight();

        int board_rows = b.getHeight();
        int board_cols = b.getWidth();

		Iterator<Tile> i = b.iterator();
		while(i.hasNext()) {
			Tile t = i.next();
			Point pos = t.getCoord();

			//Render bottom layer here
			if(t.canBeFilled()) {
				if(b.getFinishTiles().contains(t)) {
					//Finish tile
					g.setColor(Color.YELLOW);
				} else {
					//Normal FloorTile
					g.setColor(Color.LIGHT_GRAY);
				}
			} else {
				//Wall
				g.setColor(Color.RED);
			}
            g.fillRect(pos.x*panel_width/board_cols, pos.y*panel_height/board_rows,
            panel_width/board_cols,panel_height/board_rows);
        }
    }

    @Override
    public void paint(Graphics g) {
        int panel_width = this.getWidth();
        int panel_height = this.getHeight();

        int board_rows = b.getHeight();
        int board_cols = b.getWidth();

		Iterator<Tile> i = b.iterator();
		while(i.hasNext()) {
			Tile t = i.next();
			Point pos = t.getCoord();
			GamePiece contents = t.getContents();

			//Render bottom layer here
			if(t.canBeFilled()) {
				if(b.getFinishTiles().contains(t)) {
					//Finish tile
					g.setColor(Color.YELLOW);
				} else {
					//Normal FloorTile
					g.setColor(Color.LIGHT_GRAY);
				}
			} else {
				//Wall
				g.setColor(Color.RED);
			}

			//render top layer here
			if(contents != null) {
				if(contents.getType() == 0) {
					//Player
					g.setColor(Color.WHITE);
				} else {
					//Crate
					g.setColor(Color.BLUE);
				}
			}
			g.fillRect(pos.x*panel_width/board_cols, pos.y*panel_height/board_rows,
            panel_width/board_cols,panel_height/board_rows);
		}

    }

}
