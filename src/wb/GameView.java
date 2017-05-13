package wb;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import javax.imageio.ImageIO;
import java.io.*;

public class GameView extends JPanel {
	static final double scalePieces = 0.8;

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
                .withxOffset(20)
                .withSpriteSize(90,90)
                .withSpriteCount(5)
                .build();
            tiles = new SpriteSheetBuilder()
                .withSheet(ImageIO.read(new File("assets/tiles.png")))
                .withRows(1)
                .withColumns(5)
                .withxOffset(20)
                .withSpriteSize(90,90)
                .withSpriteCount(5)
                .build();
            player = new SpriteSheetBuilder()
                .withSheet(ImageIO.read(new File("assets/player_4x4_48x48.png")))
                .withRows(4)
                .withColumns(4)
                .withSpriteSize(48,48)
                .withSpriteCount(16)
                .build();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

	@Override
	public void paint(Graphics g) {

		int panel_width = this.getWidth();
		int panel_height = this.getHeight();

		int board_cols = b.getWidth();
		int board_rows = b.getHeight();

		double squareWidth = (double)panel_width/(double)board_cols;
		double squareHeight = (double)panel_height/(double)board_rows;
		// paint background tiles.
		paintBackground(g, squareWidth, squareHeight);
		paintAnimatables(g, squareWidth, squareHeight);
	}

	private void paintAnimatables(Graphics g, double squareWidth, double squareHeight) {
		for(GamePiece p : b.getPieces()) {
			paintPiece(g, p, squareWidth, squareHeight);
		}
	}

	private void paintBackground(Graphics g, double squareWidth, double squareHeight) {
		Iterator<Tile> i = b.iterator();
		while(i.hasNext()) {
			Tile t = i.next();
			paintTile(g, t, squareWidth, squareHeight);
		}
	}

	private void paintTile(Graphics g, Tile t, double squareWidth, double squareHeight)
	{
		Point pos = new Point();
		pos.setLocation(t.getCoord().getX(), t.getCoord().getY());

		if (!t.canBeFilled()) {
			// wall
			g.setColor(Color.black);
		} else if (b.getFinishTiles().contains(t)) {
			// goal
			g.setColor(Color.yellow);
		} else {
			// path
			g.setColor(Color.lightGray);
		}

		int boxWidth = (int)(squareWidth * 1);
		int boxHeight = (int)(squareHeight * 1);
		int startx = (int)(squareWidth * pos.getX() + (squareWidth-boxWidth)/2.0);
		int starty = (int)(squareHeight * pos.getY() + (squareHeight-boxHeight)/2.0);
		g.fillRect(startx, starty, boxWidth, boxHeight);
	}

	private void paintPiece(Graphics g, GamePiece p, double squareWidth, double squareHeight) {
		Point2D pos = new Point2D.Double();
		pos.setLocation(p.getCoord().getX() + p.getAnimOffset().getX(), p.getCoord().getY() + p.getAnimOffset().getY());

		if(p.getType() == 0) {
			// normal player
			g.setColor(Color.white);
		} else if(p.getType() == 1) {
			g.setColor(Color.orange);
		}

		int boxWidth = (int)(squareWidth * scalePieces);
		int boxHeight = (int)(squareHeight * scalePieces);
		int startx = (int)(squareWidth * pos.getX() + (squareWidth-boxWidth)/2.0);
		int starty = (int)(squareHeight * pos.getY() + (squareHeight-boxHeight)/2.0);
		g.fillRect(startx, starty, boxWidth, boxHeight);
	}
}
