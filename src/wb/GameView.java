package wb;

import java.io.*;
import java.util.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The game view.
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
class GameView
extends JPanel {
	private static final int PREFERRED_WIDTH = 711;
	private static final int PREFERRED_HEIGHT = 711;

	private static final String TILE_IMG_PATH = "assets/spriteSheet.png";
	private static final int TILE_IMG_ROWS = 3;
	private static final int TILE_IMG_COLS = 2;
	private static final int TILE_SPRITE_SIZE_W = 90;
	private static final int TILE_SPRITE_SIZE_H = 90;
	private static final int TILE_SPRITE_COUNT = 5;

	private static final String PLAYER_IMG_PATH = "assets/player_4x4_48x48.png";
	private static final int PLAYER_IMG_ROWS = 4;
	private static final int PLAYER_IMG_COLS = 4;
	private static final int PLAYER_SPRITE_SIZE_W = 48;
	private static final int PLAYER_SPRITE_SIZE_H = 48;
	private static final int PLAYER_SPRITE_COUNT = 16;

	private Board b;
	private JLabel gameState;
	private SpriteSheet tiles;
	private SpriteSheet player;

	/**
	 * Create a new GameView, with no board.
	 */
	public GameView() {
		this(null);
	}

	/**
	 * Create a new GameView, with the specified board.
	 *
	 * @param b the Board to render
	 */
	public GameView(Board b) {
		super();
		this.b = b;

		this.setPreferredSize(
			new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));

		this.tiles = this.loadSpriteSheet(
			TILE_IMG_PATH, TILE_IMG_ROWS, TILE_IMG_COLS,
			TILE_SPRITE_SIZE_W, TILE_SPRITE_SIZE_H, TILE_SPRITE_COUNT);
		this.player = this.loadSpriteSheet(
			PLAYER_IMG_PATH, PLAYER_IMG_ROWS, PLAYER_IMG_COLS,
			PLAYER_SPRITE_SIZE_W, PLAYER_SPRITE_SIZE_H, PLAYER_SPRITE_COUNT);

		this.gameState = new JLabel();
		this.gameState.setBackground(Color.lightGray);
		this.gameState.setOpaque(true);
		this.gameState.setFont(new Font("Arial", Font.PLAIN, 40));
		this.add(this.gameState);
	}

	/**
	 * Load a particular sprite sheet.
	 *
	 * @param path the image file to load sprites from; loaded with
	 *	   Java's ImageIO library, so any image format supported by
	 *	   this Java installation will work.
	 * @param rows the number of rows of sprites in the image.
	 * @param cols the number of columns of sprites in the image.
	 * @param width the width of an individual sprite, in pixels
	 * @param height the height of an individual sprite, in pixels
	 * @param count the number of actual sprites; should be less than
	 *	   or equal to <i>width</i> × <i>height</i>.
	 *
	 * @return a new SpriteSheet
	 */
	private SpriteSheet loadSpriteSheet(
		String path, int rows, int cols, int width, int height, int count) {

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		return new SpriteSheetBuilder()
			.withSheet(img)
			.withRows(rows)
			.withColumns(cols)
			.withSpriteSize(width, height)
			.withSpriteCount(count)
			.build();
	}

	@Override
	public void paintComponent(Graphics g) {
		if (b == null)
			return;

		int panel_width = this.getWidth();
		int panel_height = this.getHeight();

		int board_cols = b.getWidth();
		int board_rows = b.getHeight();

		double squareWidth = (double)panel_width/(double)board_cols;
		double squareHeight = (double)panel_height/(double)board_rows;

		// Paint "background" tiles.
		this.paintBackground(g, squareWidth, squareHeight);

		// Paint "foreground" tiles.
		this.paintCrates(g, squareWidth, squareHeight);
		this.paintPlayers(g, squareWidth, squareHeight);
	}

	/**
	 * Force the board to reset.
	 *
	 * @param b the new board to render
	 */
	public void resetBoard(Board b) {
		this.b = b;
		this.resizeSprites();
	}

	/**
	 * Show the game-state text label.
	 *
	 * @param msg the message to pass the user.
	 */
	public void showLabel(String msg) {
		this.gameState.setText (msg);
		this.gameState.setVisible (true);
	}

	/**
	 * Hide the game-state text label.
	 */
	public void hideLabel() {
		this.gameState.setVisible(false);
	}

	/**
	 * Paint all background tiles.
	 *
	 * @param g the graphics context to draw in
	 * @param squareWidth the width of a a square tile
	 * @param squareHeight the height of a square tile
	 */
	private void paintBackground (
		Graphics g, double squareWidth, double squareHeight) {

		Iterator<Tile> ti = this.b.tileIterator();
		while (ti.hasNext()) {
			this.paintTile(g, ti.next(), squareWidth, squareHeight);
		}
	}

	/**
	 * Paint all crates.
	 *
	 * @param g the graphics context to draw in
	 * @param squareWidth the width of a a square tile
	 * @param squareHeight the height of a square tile
	 */
	private void paintCrates(
		Graphics g, double squareWidth, double squareHeight) {

		for (Crate c : this.b.getCrates()) {
			this.paintPiece(g, c, squareWidth, squareHeight);
		}
	}

	/**
	 * Paint all players.
	 *
	 * @param g the graphics context to draw in
	 * @param squareWidth the width of a a square tile
	 * @param squareHeight the height of a square tile
	 */
	private void paintPlayers(
		Graphics g, double squareWidth, double squareHeight) {

		for (Player p : this.b.getPlayers()) {
			this.paintPiece(g, p, squareWidth, squareHeight);
		}
	}

	/**
	 * Paint one particular background tile.
	 *
	 * @param g the graphics context to draw in
	 * @param t the particular tile
	 * @param squareWidth the width of a a square tile
	 * @param squareHeight the height of a square tile
	 */
	private void paintTile (
		Graphics g, Tile t, double squareWidth, double squareHeight) {

		Point pos = new Point();
		pos.setLocation(t.getCoord().getX(), t.getCoord().getY());
		int boxWidth = (int)(squareWidth * 1);
		int boxHeight = (int)(squareHeight * 1);
		int startx = (int)(squareWidth * pos.getX() + (squareWidth - boxWidth)/2.0);
		int starty = (int)(squareHeight * pos.getY() + (squareHeight - boxHeight)/2.0);

		if (!t.canBeFilled()) {
			// wall
			g.drawImage(tiles.getScaled(2), startx, starty, null);

		} else if (b.getFinishTiles().contains(t)) {
			// goal
			g.drawImage(tiles.getScaled(4), startx, starty, null);

		} else {
			// empty tile
			g.drawImage(tiles.getScaled(0), startx, starty, null);
		}
	}

	/**
	 * Paint one particular game piece.
	 *
	 * @param g the graphics context to draw in
	 * @param p the particular game piece.
	 * @param squareWidth the width of a a square tile
	 * @param squareHeight the height of a square tile
	 */
	private void paintPiece (
		Graphics g, GamePiece p, double squareWidth, double squareHeight) {

		Point2D pos = new Point2D.Double();
		pos.setLocation(
			p.getCoord().getX() + p.getAnimOffset().getX(),
			p.getCoord().getY() + p.getAnimOffset().getY());
		Image curr = null;

		if(p.getType() == 0) {
			// normal player
			curr = player.animate((Player)p);

		} else if(p.getType() == 1) {
			// crate
			curr = tiles.getScaled(1);
		}

		if (curr == null) {
			return;
		}

		int width = curr.getWidth(null);
		int height = curr.getHeight(null);

		int startx = (int)(squareWidth * pos.getX() + (squareWidth-width)/2.0);
		int starty = (int)(squareHeight * pos.getY() + (squareHeight-height)/2.0);

		g.drawImage(curr, startx, starty, null);

	}

	/**
	 * Request that the sprite sheets trigger resize events.
	 */
	public void resizeSprites() {
		if (b == null) {
			return;
		}

		int panel_width = this.getWidth();
		int panel_height = this.getHeight();

		int board_cols = b.getWidth();
		int board_rows = b.getHeight();
		//System.out.println(panel_width + " " + panel_height);

		int squareWidth = (int)((double)panel_width/(double)board_cols);
		int squareHeight = (int)((double)panel_height/(double)board_rows);

		this.tiles.resize(squareWidth, squareHeight);
		this.player.resize(squareWidth, squareHeight);
	}
}
