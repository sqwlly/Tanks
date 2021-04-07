package sample.auxiliary.state;

import sample.auxiliary.Constant;
import sample.auxiliary.GameStateManager;
import sample.auxiliary.Progress;
import sample.auxiliary.ResourceLoader;
import sample.auxiliary.graphics.Sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SettleScoreState extends GameState {
    private GameStateManager gsm;

    private Progress pr;
    private Sprite sprite;
    private BufferedImage coin;

    private int levelID;
    private int levelLineWidth;

    private Font font;


    public SettleScoreState(GameStateManager gsm) {
        this.gsm = gsm;
        pr = Progress.getInstance();

        levelID = Integer.parseInt(pr.get("levelToPlay"));
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.loadFontStream("joystix.ttf")).deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        levelLineWidth = (int)((Constant.FRAME_WIDTH / (double)Integer.parseInt(pr.get("xpLevelMaxScore")))
                *(double) Integer.parseInt(pr.get("xp")));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gsm.setGameState(STATE.STAGE);
    }

    @Override
    public void drawImage(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);

        g.setFont(font.deriveFont(20f));
        g.setColor(Color.WHITE);
        g.drawString("STAGE " + levelID, Constant.ELEMENT_SIZE * 6 - 17, Constant.ELEMENT_SIZE * 2);

        g.setColor(Color.RED);
        g.drawString("I-PLAYER", Constant.ELEMENT_SIZE * 2 - 17, Constant.ELEMENT_SIZE * 4);
        g.setColor(Color.YELLOW.darker());
        g.drawString(pr.get("currentScore"), Constant.ELEMENT_SIZE * 5, Constant.ELEMENT_SIZE * 5);

        g.setColor(Color.WHITE);
        int killed = 0;
        for (int i = 0; i < 4; i++) {
            g.drawString(Integer.parseInt(pr.get("killed" + (i + 1))) * (i + 1) + (Integer.parseInt(pr.get("killed" + (i + 1))) == 0 ? "" : "00"),
                    Constant.ELEMENT_SIZE * 5, Constant.ELEMENT_SIZE * 6 + i * Constant.ELEMENT_SIZE);
            g.drawString("PTS", Constant.ELEMENT_SIZE * 7, Constant.ELEMENT_SIZE * 6 + i * Constant.ELEMENT_SIZE);
            g.drawString(pr.get("killed" + (i + 1)), Constant.ELEMENT_SIZE * 10, Constant.ELEMENT_SIZE * 6 + i * Constant.ELEMENT_SIZE);
            //g.drawImage(sprite.getImage(0, i), 780, 310+i*70, 50, 50, null);
            killed += Integer.parseInt(pr.get("killed" + (i + 1)));
        }
        g.fillRect(Constant.ELEMENT_SIZE * 2 + 17, Constant.ELEMENT_SIZE * (6 + 4), Constant.FRAME_WIDTH * 2 / 3, 6);
        g.drawString("TOTAL  " + killed, Constant.ELEMENT_SIZE * 6 + 17, Constant.ELEMENT_SIZE * 11);
  //      g.drawString("COINS   10", Constant.ELEMENT_SIZE * 2 + Constant.ELEMENT_SIZE / 2, Constant.ELEMENT_SIZE * 6 + Constant.ELEMENT_SIZE / 2);

        g.setColor(Color.decode("#636363"));
        g.setFont(font.deriveFont(10f));
        g.drawString("Click to continue", Constant.ELEMENT_SIZE * 3, Constant.ELEMENT_SIZE * 12);

        //g.drawImage(coin, 790, 670, 30, 30, null);
    }
}
