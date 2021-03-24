package sample.auxiliary.graphics;

import sample.auxiliary.Constant;
import sample.auxiliary.ResourceLoader;

import java.awt.image.BufferedImage;

public class TextureAtlas {
    private static BufferedImage atlas = ResourceLoader.loadImage(Constant.ATLAS_FILE_NAME);

    public static BufferedImage cut(int x, int y, int width, int height) {
        return atlas.getSubimage(x, y, width, height);
    }
}
