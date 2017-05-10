package wb;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

/**
 * SpriteSheet is BufferedImage used to store game assets
 * sourced from here:
 * http://stackoverflow.com/questions/35472233/load-a-sprites-image-in-java
 */
public class SpriteSheet {

	private final List<BufferedImage> sprites;

    public SpriteSheet(List<BufferedImage> sprites) {
        this.sprites = new ArrayList<>(sprites);
    }

    public int count() {
        return sprites.size();
    }

    public BufferedImage getSprite(int index) {
        return sprites.get(index);
    }
}
