package sample.auxiliary.graphics;

import sample.auxiliary.Constant;

import java.awt.image.BufferedImage;

public class SpriteSheet {
    private BufferedImage sheet;
    private int spriteCount;
    private int w, h;
    private int spritesInWidth;

    public SpriteSheet(BufferedImage sheet, int w, int h) {
        this.sheet = sheet;
        this.w = w;
        this.h = h;
        this.spritesInWidth = sheet.getWidth() / w;
        this.spriteCount = spritesInWidth * (sheet.getHeight() / h);
//        System.out.println("spriteCount = " + spriteCount);
    }

    /**
     * @Description 获取Sprite组件里的其中一个
     * @Param [index]
     * @return java.awt.image.BufferedImage
     */
    public BufferedImage getSprite(int index) {
        index = index % spriteCount;
        int x = index % spritesInWidth * w;
        int y = index / spritesInWidth * h;
        return sheet.getSubimage(x, y, w, h);
    }

    public int getW() {
        return w;
    }

    public int getH() { return h; }

    public int getSpriteCount() {
        return spriteCount;
    }
}
