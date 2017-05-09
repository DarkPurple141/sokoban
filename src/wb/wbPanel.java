package wb;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Color;

public class wbPanel extends JPanel {

    Board b;

    public wbPanel (Board b) {
        super();
        this.b = b;
    }

    public void paint(Graphics g) {
        int panel_width = this.getWidth();
        int panel_height = this.getHeight();
        Tile[][] tiles = b.getTiles();
        int board_rows = b.getHeight();
        int board_cols = b.getWidth();

        int height = 0;
        for (Tile[] row : tiles) {
            int width = 0;
            for (Tile col : row) {
                //col.get()
                if (col instanceof FinishTile) {
                    g.setColor(Color.YELLOW);
                    g.fillRect(width,height,panel_width/board_cols,panel_height/board_rows);
                } else {
                    if (col instanceof ContainerTile) {
                        if (((ContainerTile)col).getContents() instanceof Crate) {
                            g.setColor(Color.BLUE);
                            g.fillRect(width,height,panel_width/board_cols,panel_height/board_rows);
                        } else if (((ContainerTile)col).getContents() instanceof Player) {
                            g.setColor(Color.WHITE);
                            g.fillRect(width,height,panel_width/board_cols,panel_height/board_rows);
                        }
                    } else {
                        g.setColor(Color.RED);
                        g.fillRect(width,height,panel_width/board_cols,panel_height/board_rows);
                    }

                }
                width += panel_width/board_cols;
            }
            height += panel_height/board_rows;
        }

    }

}
