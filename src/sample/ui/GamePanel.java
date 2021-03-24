package sample.ui;

import sample.base.IDraw;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private IDraw[] draws;//待绘制的元素
    private Image image; //缓冲
    public GamePanel(IDraw... draws) {
        this.draws = draws;
    }

    /**
     * @Description 绘制缓冲
     * @Param []
     * @return void
     */
    private void drawBufferedImage() {
        image = createImage(this.getWidth(), this.getHeight());
        Graphics g = image.getGraphics();
        for(IDraw draw : this.draws) {
            draw.drawImage(g);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawBufferedImage();
        g.drawImage(image, 0, 0, this);
    }
}
