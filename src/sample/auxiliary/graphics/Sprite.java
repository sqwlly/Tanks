package sample.auxiliary.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite {
    protected BufferedImage image;
    protected SpriteSheet sheet;
    protected int index;
    public Sprite(SpriteSheet sheet, float scale, int index) {
        this.sheet = sheet;
        this.index = index % sheet.getW();
        BufferedImage origin = this.sheet.getSprite(index);
        BufferedImage actual = new BufferedImage((int)(origin.getWidth() * scale), (int)(origin.getHeight() * scale), BufferedImage.TYPE_INT_ARGB);
        actual.getGraphics().drawImage(origin, 0, 0, actual.getWidth(), actual.getHeight(), null);
        this.image = actual;
    }

    public void render(Graphics g, int x, int y) {
        g.drawImage(this.image, x, y, this.image.getWidth(), this.image.getHeight(), null);
    }

    public void render(Graphics g, int x, int y, int w, int h) {
        g.drawImage(this.image, x, y, w, h, null);
    }

    public void transparentColor(int color, float opacity) {
        int opacityRate = (int) ((0xff / 100.0f) * (opacity * 100));

        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] == color) {
                pixels[i] = (opacityRate << 24) | (pixels[i] & 0x00ffffff);
            }
        }

        image.setRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
    }
}
