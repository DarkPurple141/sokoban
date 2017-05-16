package wb;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import java.io.*;

public class GameView extends JPanel {
	static final double scalePieces = 0.8;

    private Board b;
	private JLabel gameState;
    private SpriteSheet crates;
    private SpriteSheet tiles;
    private SpriteSheet player;
    private static final long serialVersionUID = 11; // apparently swing needs this

    public GameView(Board b) {
        super();
        this.b = b;
        GameViewBuilder();
    }

	public void resetBoard(Board b) {
		this.b = b;
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
		gameState = new JLabel();
		this.add(gameState);
    }

	@Override
	public void paintComponent(Graphics g) {

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

	public void showLabel(String text) {
		gameState.setFont(new Font("Arial", Font.PLAIN, 50));
		gameState.setText(text);
		gameState.setVisible(true);
	}

	public void hideLabel() {
		gameState.setVisible(false);
	}

	private void paintAnimatables(Graphics g, double squareWidth, double squareHeight) {
		for(GamePiece p : b.getCrates()) {
			paintPiece(g, p, squareWidth, squareHeight);
		}
		for(GamePiece p : b.getPlayers()) {
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
		int boxWidth = (int)(squareWidth * 1);
		int boxHeight = (int)(squareHeight * 1);
		int startx = (int)(squareWidth * pos.getX() + (squareWidth-boxWidth)/2.0);
		int starty = (int)(squareHeight * pos.getY() + (squareHeight-boxHeight)/2.0);

		if (!t.canBeFilled()) {
			// wall
			//g.setColor(Color.black);
			g.drawImage(crates.getScaled(0),startx,starty,null);
			//return;

		} else if (b.getFinishTiles().contains(t)) {
			// goal
			//g.drawImage(crates.getSprite(1),startx,starty,null);
			//return;
			//g.setColor(Color.yellow);
			g.drawImage(crates.getScaled(1),startx,starty,null);
		} else {
			// path
			//g.drawImage(tiles.getSprite(0),startx,starty,null);
			//Image scaled = tiles.getSprite(0).getScaledInstance(boxWidth, boxHeight, Image.SCALE_SMOOTH);
			g.drawImage(tiles.getScaled(0),startx,starty,null);
			//g.setColor(Color.lightGray);
		}

		//g.fillRect(startx, starty, boxWidth, boxHeight);
	}

	private void paintPiece(Graphics g, GamePiece p, double squareWidth, double squareHeight) {
		Point2D pos = new Point2D.Double();
		pos.setLocation(p.getCoord().getX() + p.getAnimOffset().getX(),
			p.getCoord().getY() + p.getAnimOffset().getY());

		int boxWidth = (int)(squareWidth * scalePieces);
		int boxHeight = (int)(squareHeight * scalePieces);
		int startx = (int)(squareWidth * pos.getX() + (squareWidth-boxWidth)/2.0);
		int starty = (int)(squareHeight * pos.getY() + (squareHeight-boxHeight)/2.0);
		if(p.getType() == 0) {
			// normal player
			g.setColor(Color.white);
			g.drawImage(player.animate(p),startx,starty,null);
			return;
		} else if(p.getType() == 1) {
			g.setColor(Color.orange);
		}

		g.fillRect(startx, starty, boxWidth, boxHeight);
	}

	public void resizeSprites() {
		System.out.println("Resize event!");
		int panel_width = this.getWidth();
		int panel_height = this.getHeight();

		int board_cols = b.getWidth();
		int board_rows = b.getHeight();
		
		int squareWidth = (int)((double)panel_width/(double)board_cols);
		int squareHeight = (int)((double)panel_height/(double)board_rows);
		
		crates.resize(squareWidth, squareHeight);
		tiles.resize(squareWidth, squareHeight);
		
	}
}
