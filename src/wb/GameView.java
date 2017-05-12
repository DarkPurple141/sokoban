package wb;
import javax.swing.JPanel;
import java.awt.*;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.imageio.ImageIO;
import java.io.*;

public class GameView extends JPanel {

    private Board b;
    private SpriteSheet crates;
    private SpriteSheet tiles;
    private SpriteSheet player;

    public GameView(Board b) {
        super();
        this.b = b;
        GameViewBuilder();
    }

    private void GameViewBuilder() {
        try {
            crates = new SpriteSheetBuilder()
                .withSheet(ImageIO.read(new File("assets/crates_walls.png")))
                .withRows(1)
                .withColumns(5)
                .withxOffset(22)
                .withSpriteCount(5)
                .build();
            tiles = new SpriteSheetBuilder()
                .withSheet(ImageIO.read(new File("assets/tiles.png")))
                .withRows(1)
                .withColumns(5)
                .withxOffset(22)
                .withSpriteCount(5)
                .build();
            player = new SpriteSheetBuilder()
                .withSheet(ImageIO.read(new File("assets/player_4x4_48x48.png")))
                .withRows(4)
                .withColumns(4)
                .withSpriteCount(16)
                .build();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
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

        paintBackground(g);

		Iterator<Tile> i = b.iterator();
		while(i.hasNext()) {
			Tile t = i.next();
			Point pos = t.getCoord();
			GamePiece contents = t.getContents();

			//render top layer here
			if(contents != null) {
				if(contents.getType() == 0) {
					//Player
					g.setColor(Color.WHITE);
				} else {
					//Crate
					g.setColor(Color.BLUE);
				}
                g.fillRect(pos.x*panel_width/board_cols, pos.y*panel_height/board_rows,
                panel_width/board_cols,panel_height/board_rows);
			}

		}

    }

}
