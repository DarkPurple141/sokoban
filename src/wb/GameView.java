package wb;
import javax.swing.JPanel;
import java.awt.*;
import java.util.*;
import javax.swing.BorderFactory;
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

		Iterator<Tile> i = b.iterator();
		while(i.hasNext()) {
			Tile t = i.next();
			paintTile(g, t, squareWidth, squareHeight);
		}
		java.util.List<GamePiece> pieces = new ArrayList<>();
		pieces.addAll(b.getCrates());
		pieces.addAll(b.getPlayers());
		for(GamePiece p : pieces) {
			paintPiece(g, p, squareWidth, squareHeight);
		}
	}

	private void paintTile(Graphics g, Tile t, double squareWidth, double squareHeight)
	{
		Point pos = t.getCoord();

		if (!t.canBeFilled()) {
			g.setColor(Color.black);
		} else if (b.getFinishTiles().contains(t)) {
			g.setColor(Color.yellow);
		} else {
			g.setColor(Color.lightGray);
		}

		int boxWidth = (int)(squareWidth * 1);
		int boxHeight = (int)(squareHeight * 1);
		int startx = (int)(squareWidth * pos.getX() + (squareWidth-boxWidth)/2.0);
		int starty = (int)(squareHeight * pos.getY() + (squareHeight-boxHeight)/2.0);
		g.fillRect(startx, starty, boxWidth, boxHeight);
	}

	private void paintPiece(Graphics g, GamePiece p, double squareWidth, double squareHeight) {
		Point pos = p.getCoord();
		pos.setLocation(pos.getX() + p.getAnimOffset().getX(), pos.getY() + p.getAnimOffset().getY());

		if(p.getType() == 0) {
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
