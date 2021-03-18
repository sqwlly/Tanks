package sample.auxiliary.graphics;

import sample.auxiliary.ResourceLoader;

import java.awt.image.BufferedImage;

//Sprite compose auxiliary
public class TextureAtlas {
    BufferedImage image;
    public TextureAtlas(String imageName) {
        image = ResourceLoader.loadImage(imageName);
    }

    public BufferedImage cut(int x, int y, int w, int h) {
        return image.getSubimage(x, y, w, h);
    }
}
