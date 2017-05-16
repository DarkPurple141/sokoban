package wb;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.awt.Image;

/**
 * SpriteSheet is BufferedImage used to store game assets
 * sourced from here:
 * http://stackoverflow.com/questions/35472233/load-a-sprites-image-in-java
 */
public class SpriteSheet {

	private final List<BufferedImage> sprites;
	private List<Image> scaled;
	private int curr;
	enum directions {UP, DOWN, LEFT, RIGHT};

    public SpriteSheet(List<BufferedImage> sprites) {
        this.sprites = new ArrayList<>(sprites);
        this.scaled = new ArrayList<Image>();
		this.curr = 0;
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

		if (!p.isMoving()) {
			curr = (direction + 2) % 4;
		} else {
			switch(direction) {
				case 0:
					if ((curr + 2) % 4 != 0) {
						curr = 2;
					}
					curr = (curr + 4) % 16;
					break;
				case 1:
					if ((curr + 1) % 4 != 0) {
						curr = 3;
					}
					curr = (curr + 4) % 16;
					break;
				case 2:
					if (curr % 4 != 0) {
						curr = 0;
					}
					curr = (curr + 4) % 16;
					break;
				case 3:
					if ((curr + 3) % 4 != 0) {
						curr = 1;
					}
					curr = (curr + 4) % 16;
					break;
			}
		}

		return scaled.get(curr);

	}
}
