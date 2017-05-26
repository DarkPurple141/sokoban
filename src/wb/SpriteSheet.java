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

	public SpriteSheet(List<BufferedImage> sprites) {
		this.sprites = new ArrayList<>(sprites);
		this.scaled = new ArrayList<Image>();
		this.animationState = 0;
	}

	public int count() {
		return sprites.size();
	}

	public void resize(int width, int height) {
		scaled.clear();
		for (BufferedImage curr: sprites) {
			scaled.add(curr.getScaledInstance(width, height, Image.SCALE_SMOOTH));
		}
	}

	public Image getScaled(int index) {
		return scaled.get(index);
	}

	public BufferedImage getSprite(int index) {
		return sprites.get(index);
	}

	public Image animate(Player p) {
		int direction = p.getDirection();
		boolean moving = p.isMoving();

		int x = (direction + 2) % 4;
		int y = moving ? animationState+1 : 0;

		animationState = (animationState + 1) % 3;//Maximum of 3 moving animation frames

		return scaled.get(4 * y + x);

	}
}
