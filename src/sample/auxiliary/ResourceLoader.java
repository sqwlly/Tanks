package sample.auxiliary;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ResourceLoader {
    public static final String PATH = "resources/";
    public static BufferedImage loadImage(String fileName) {
        BufferedImage bufferedImage = null;
        try {
//            System.out.println(PATH + fileName);
            bufferedImage = ImageIO.read(new File(PATH + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }
}
