package wb;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.awt.Image;

/**
 * SpriteSheet is BufferedImage used to store game assets.
 *
 * adapted from here:
 * http://stackoverflow.com/questions/35472233/load-a-sprites-image-in-java
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 */
class SpriteSheet {

	private final List<BufferedImage> sprites;
	private List<Image> scaled;
	private int animationState;

	/**
	 * Initialises the sprate sheet
	 * @param sprites
	 */
	public SpriteSheet(List<BufferedImage> sprites) {
		this.sprites = new ArrayList<>(sprites);
		this.scaled = new ArrayList<Image>();
		this.animationState = 0;
	}

	/**
	 * Counts the number of sprites
	 * @return the number of sprites
	 */
	public int count() {
		return sprites.size();
	}

	/**
	 * Resizes the sprite sheet to the given number of rows and columns on the screen
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height) {
		scaled.clear();
		for (BufferedImage curr: sprites) {
			scaled.add(curr.getScaledInstance(width, height, Image.SCALE_SMOOTH));
		}
	}

	/**
	 * Returns one scales sprite
	 * @param index the sprite to scale
	 * @return the Image of the sprite
	 */
	public Image getScaled(int index) {
		return scaled.get(index);
	}

	/**
	 * Returns one of the existing sprites
	 * @param index the index of the sprite to get
	 * @return The image of the sprite
	 */
	public BufferedImage getSprite(int index) {
		return sprites.get(index);
	}

	/**
	 * Animates the player sprite based on the animationState variable
	 * This is looped every 4 times this is called
	 * @param p the player to animate
	 * @return the next frame of the player animation
	 */
	public Image animate(Player p) {
		Direction dir = p.getDirection();
		boolean moving = p.isMoving();

		int x = Direction.dir2Int(Direction.reverseDir(dir));
		int y = moving ? animationState+1 : 0;

		animationState = (animationState + 1) % 3;//Maximum of 3 moving animation frames

		return scaled.get(4 * y + x);

	}
}
