package wb;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public class SpriteSheetBuilder {

    private BufferedImage spriteSheet;
    private int rows, cols;
    private int widthOffset, heightOffset;
    private int spriteWidth, spriteHeight;
    private int spriteCount;

    public SpriteSheetBuilder withSheet(BufferedImage img) {
        spriteSheet = img;
        widthOffset = 0;
        heightOffset = 0;
        return this;
    }

    public SpriteSheetBuilder withRows(int rows) {
        this.rows = rows;
        return this;
    }

    public SpriteSheetBuilder withxOffset(int x) {
        this.widthOffset = x;
        return this;
    }

    public SpriteSheetBuilder withyOffset(int y) {
        this.heightOffset = y;
        return this;
    }

    public SpriteSheetBuilder withColumns(int cols) {
        this.cols = cols;
        return this;
    }

    public SpriteSheetBuilder withSpriteSize(int width, int height) {
        this.spriteWidth = width;
        this.spriteHeight = height;
        return this;
    }

    public SpriteSheetBuilder withSpriteCount(int count) {
        this.spriteCount = count;
        return this;
    }

    protected int getSpriteCount() {
        return spriteCount;
    }

    protected int getCols() {
        return cols;
    }

    protected int getRows() {
        return rows;
    }

    protected int getSpriteHeight() {
        return spriteHeight;
    }

    protected BufferedImage getSpriteSheet() {
        return spriteSheet;
    }

    protected int getSpriteWidth() {
        return spriteWidth;
    }

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
