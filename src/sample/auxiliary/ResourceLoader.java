package sample.auxiliary;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

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

    public static BufferedReader loadMapConfig(String fileName) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(PATH + fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bufferedReader;
    }

    public static InputStream loadFontStream(String fileName) {
        InputStream stream = null;
        try {
            stream = new FileInputStream("resources/joystix.ttf");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }
}
