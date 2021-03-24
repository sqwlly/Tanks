package sample.auxiliary.graphics;

import sample.auxiliary.Constant;

import java.awt.image.BufferedImage;

public class SpriteSheet {
    private BufferedImage sheet;
    private int spriteCount;
    private int size;
    private int spritesInWidth;

    public SpriteSheet(BufferedImage sheet, int size) {
        this.sheet = sheet;
        this.size = size;
        this.spritesInWidth = sheet.getWidth() / size;
        this.spriteCount = spritesInWidth * (sheet.getHeight() / size);
//        System.out.println("spriteCount = " + spriteCount);
    }

    /**
     * @Description 获取Sprite组件里的其中一个
     * @Param [index]
     * @return java.awt.image.BufferedImage
     */
    public BufferedImage getSprite(int index) {
        index = index % spriteCount;
        int x = index % spritesInWidth * size;
        int y = index / spritesInWidth * size;
        return sheet.getSubimage(x, y, size, size);
    }

    public int getSize() {
        return size;
    }

    public int getSpriteCount() {
        return spriteCount;
    }
}
