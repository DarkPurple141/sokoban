package wb;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Color;

public class wbPanel extends JPanel {

    private int rows;
    private int cols;
    private View parent;
    private static int SCREEN_WIDTH;
    private static int SCREEN_HEIGHT;

    public wbPanel (int width, int height,int rows,int cols,View v) {
        super();
        this.rows = rows;
        this.cols = cols;
        this.SCREEN_WIDTH = width;
        this.SCREEN_HEIGHT = height;
        this.parent = v;
    }

    public void paint(Graphics g) {
        Tile[][] tiles = parent.getBoard().getTiles();

        int height = 0;
        for (Tile[] row : tiles) {
            int width = 0;
            for (Tile col : row) {
                //col.get()
                if (col instanceof FinishTile) {
                    g.setColor(Color.YELLOW);
                    g.fillRect(width,height,SCREEN_WIDTH/3,SCREEN_WIDTH/3);
                } else {
                    if (col instanceof ContainerTile) {
                        if (((ContainerTile)col).getContents() instanceof Crate) {
                            g.setColor(Color.BLUE);
                            g.fillRect(width,height,SCREEN_WIDTH/3,SCREEN_WIDTH/3);
                        } else if (((ContainerTile)col).getContents() instanceof Player) {
                            g.clearRect(width,height,SCREEN_WIDTH/3,SCREEN_WIDTH/3);
                        }
                    }

                }
                width += SCREEN_WIDTH/3;
            }
            height += SCREEN_HEIGHT/3;
        }
        //g.fillRect(100,100,400,400);
    }

}
