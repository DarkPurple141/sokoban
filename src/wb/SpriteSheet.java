package wb;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.awt.geom.Point2D;

/**
 * SpriteSheet is BufferedImage used to store game assets
 * sourced from here:
 * http://stackoverflow.com/questions/35472233/load-a-sprites-image-in-java
 */
public class SpriteSheet {

	private final List<BufferedImage> sprites;
	private int curr;

    public SpriteSheet(List<BufferedImage> sprites) {
        this.sprites = new ArrayList<>(sprites);
		this.curr = 0;
    }

    public int count() {
        return sprites.size();
    }

    public BufferedImage getSprite(int index) {
        return sprites.get(index);
    }

	public BufferedImage animate(GamePiece p) {
		double currX = p.getAnimOffset().getX();
		double currY = p.getAnimOffset().getY();

		if (currY == 0.0 && currX == 0.0) {
			curr = 0;
		} else if (currX == 0.0) {
			if (currY > 0.0) {
				if ((curr + 2) % 4 != 0) {
					curr = 2;
				}
				curr = (curr + 4) % 16;
			} else {
				if (curr % 4 != 0) {
					curr = 1;
				}
				curr = (curr + 4) % 16;
			}
		} else if (currY == 0.0) {
			if (currX > 0.0) {
				if ((curr + 1) % 4 != 0) {
					curr = 1;
				}
				curr = (curr + 4) % 16;
			} else {
				if ((curr + 3) % 4 != 0) {
					curr = 3;
				}
				curr = (curr + 4) % 16;
			}
		}

		return sprites.get(curr);


	}
}
