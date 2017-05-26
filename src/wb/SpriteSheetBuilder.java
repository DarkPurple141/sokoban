package wb;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

/**
 * SpriteSheet is BufferedImage used to store game assets
 *
 * sourced and adapted from here:
 * http://stackoverflow.com/questions/35472233/load-a-sprites-image-in-java
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 */
class SpriteSheetBuilder {
	private BufferedImage spriteSheet;
	private int rows, cols;
	private int widthOffset, heightOffset;
	private int spriteWidth, spriteHeight;
	private int spriteCount;

	/**
	 * Loads the sprite image into the builder
	 * @param img the image to load
	 * @return
	 */
	public SpriteSheetBuilder withSheet(BufferedImage img) {
		spriteSheet = img;
		this.widthOffset = 0;
		this.heightOffset = 0;
		return this;
	}

	/**
	 * Sets the number of rows on the image
	 * @param rows number of rows
	 * @return
	 */
	public SpriteSheetBuilder withRows(int rows) {
		this.rows = rows;
		return this;
	}

	/**
	 * Sets the x offset of the start of sprites
	 * @param x offset in pixels
	 * @return
	 */
	public SpriteSheetBuilder withxOffset(int x) {
		this.widthOffset = x;
		return this;
	}

	/**
	 * Sets the y offset of the start of sprites
	 * @param y offset in pixels
	 * @return
	 */
	public SpriteSheetBuilder withyOffset(int y) {
		this.heightOffset = y;
		return this;
	}

	/**
	 * Sets the number of columns on the image
	 * @param cols numer of columns
	 * @return
	 */
	public SpriteSheetBuilder withColumns(int cols) {
		this.cols = cols;
		return this;
	}

	/**
	 * Sets the size of each sprite on the image
	 * @param width width in pixels
	 * @param height height in pixels
	 * @return
	 */
	public SpriteSheetBuilder withSpriteSize(int width, int height) {
		this.spriteWidth = width;
		this.spriteHeight = height;
		return this;
	}

	/**
	 * Sets the number of sprites on the image
	 * @param count
	 * @return
	 */
	public SpriteSheetBuilder withSpriteCount(int count) {
		this.spriteCount = count;
		return this;
	}

	/**
	 * Gets the sprite count
	 * @return the sprite count
	 */
	protected int getSpriteCount() {
		return spriteCount;
	}

	/**
	 * Gets the number of columns
	 * @return number of columns
	 */
	protected int getCols() {
		return cols;
	}

	/**
	 * Gets the number of rows
	 * @return number of rows
	 */
	protected int getRows() {
		return rows;
	}

	/**
	 * Gets the height of each sprite
	 * @return height of each sprite
	 */
	protected int getSpriteHeight() {
		return spriteHeight;
	}

	/**
	 * Returns the entire sprite sheet
	 * @return the build sprite sheet
	 */
	protected BufferedImage getSpriteSheet() {
		return spriteSheet;
	}

	/**
	 * Gets the width of each sprite
	 * @return width of each sprite
	 */
	protected int getSpriteWidth() {
		return spriteWidth;
	}

	/**
	 * Builds the sprite sheet based on the given parameters
	 * @return the completed sprite sheet
	 */
	public SpriteSheet build() {
		int count = getSpriteCount();
		int rows = getRows();
		int cols = getCols();
		if (count == 0) {
			count = rows * cols;
		}

		BufferedImage sheet = getSpriteSheet();

		int width = getSpriteWidth();
		int height = getSpriteHeight();
		if (width == 0) {
			width = sheet.getWidth() / cols;
		}
		if (height == 0) {
			height = sheet.getHeight() / rows;
		}

		int x = 0;
		int y = 0;
		List<BufferedImage> sprites = new ArrayList<>(count);

		for (int index = 0; index < count; index++) {
			sprites.add(sheet.getSubimage(x, y, width, height));
			x += width + this.widthOffset;
			if (x >= width * cols) {
				x = 0;
				y += height + this.heightOffset;
			}
		}

		return new SpriteSheet(sprites);
	}
}
