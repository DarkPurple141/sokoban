package wb;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Iterator;
import java.awt.Point;

public class wbPanel extends JPanel {

    Board b;

    public wbPanel (Board b) {
        super();
        this.b = b;
    }

    public void paint(Graphics g) {
        int panel_width = this.getWidth();
        int panel_height = this.getHeight();
        //Tile[][] tiles = b.getTiles();
        int board_rows = b.getHeight();
        int board_cols = b.getWidth();
        Iterator<Tile> tiles = b.iterator();
        int TILE_HEIGHT = panel_height/board_rows;
        int TILE_WIDTH = panel_width/board_cols;

        while (tiles.hasNext()) {
            Tile curr = tiles.next();
            Point p = curr.getCoord();
            int x = p.x; int y = p.y;
            System.err.println(x + " " + y);
            if (curr instanceof FinishTile) {
                // goal
                g.setColor(Color.YELLOW);
                g.fillRect(x*TILE_WIDTH,y*TILE_HEIGHT,TILE_WIDTH,TILE_HEIGHT);
            } else {

                if (curr.getContents() == null){
                    // walls
                    g.setColor(Color.GRAY);
                    g.fillRect(x*TILE_WIDTH,y*TILE_HEIGHT,TILE_WIDTH,TILE_HEIGHT);
                } else {
                    if (curr.getContents().getType() == 1) {
                        // passable no player
                        g.setColor(Color.BLUE);
                        g.fillRect(x*TILE_WIDTH,y*TILE_HEIGHT,TILE_WIDTH,TILE_HEIGHT);
                    } else if (curr.getContents().getType() == 0) {
                        // player
                        g.setColor(Color.WHITE);
                        g.fillRect(x*TILE_WIDTH,y*TILE_HEIGHT,TILE_WIDTH,TILE_HEIGHT);
                    }


                }
            }

        }

        /*
        int height = 0;
        for (Tile[] row : tiles) {
            int width = 0;
            for (Tile col : row) {
                //col.get()
                if (col instanceof FinishTile) {
                    g.setColor(Color.YELLOW);
                    g.fillRect(width,height,panel_width/board_cols,panel_height/board_rows);
                } else {
                    if (col.get){
                        g.setColor(Color.RED);
                        g.fillRect(width,height,panel_width/board_cols,panel_height/board_rows);
                    } else (col.canBeFilled()) {
                        if (col.getContents().getType() == 1) {
                            g.setColor(Color.BLUE);
                            g.fillRect(width,height,panel_width/board_cols,panel_height/board_rows);
                        } else if (col.getContents().getType() == 0) {
                            g.setColor(Color.WHITE);
                            g.fillRect(width,height,panel_width/board_cols,panel_height/board_rows);
                        }
                    }

                }
                System.err.println(col);
                width += panel_width/board_cols;
            }
            height += panel_height/board_rows;

        }*/


    }

}
