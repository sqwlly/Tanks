package sample.auxiliary.state;

import sample.auxiliary.Constant;
import sample.auxiliary.GameStateManager;
import sample.auxiliary.Progress;
import sample.auxiliary.ResourceLoader;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SettleScoreState extends GameState {
    private GameStateManager gsm;

    private Progress pr;
    private final List<Sprite> sprites = new ArrayList<>();
    private BufferedImage coin;

    private int levelID;
    private int levelLineWidth;

    public SettleScoreState(GameStateManager gsm) {
        this.gsm = gsm;
        pr = Progress.getInstance();
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(0, Constant.ELEMENT_SIZE * 2,
                Constant.ELEMENT_SIZE * 32, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        for(int i = 0; i < 4; ++i) {
            sprites.add(new Sprite(sheet, 1, i * 8));
        }
        levelID = Integer.parseInt(pr.get("levelToPlay"));
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
    public void stateAction() {

    }

    @Override
    public void drawImage(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);

        g.setFont(font.deriveFont(20f));
        g.setColor(Color.WHITE);
        g.drawString("STAGE " + levelID, Constant.ELEMENT_SIZE * 6 - 17, Constant.ELEMENT_SIZE * 3 / 2);

        g.setColor(Color.RED);
        g.drawString("I-PLAYER", Constant.ELEMENT_SIZE * 2 - 17, Constant.ELEMENT_SIZE * 3);
        g.setColor(Color.YELLOW.darker());
        int tx = Constant.ELEMENT_SIZE * 3;
        g.drawString(pr.get("currentScore"), tx, Constant.ELEMENT_SIZE * 4);

        g.setColor(Color.WHITE);
        int killed = 0;
        for (int i = 0; i < 4; i++) {
            int ty = Constant.ELEMENT_SIZE * 5 + i * (Constant.ELEMENT_SIZE + 10) + 10;
            g.drawString(Integer.parseInt(pr.get("killed" + (i + 1))) * (i + 1) + (Integer.parseInt(pr.get("killed" + (i + 1))) == 0 ? "" : "00"),
                    tx, ty);
            g.drawString("PTS", tx + Constant.ELEMENT_SIZE * 3, ty);
            g.drawString(pr.get("killed" + (i + 1)), tx + Constant.ELEMENT_SIZE * 6, ty);
            sprites.get(i).render(g, tx + Constant.ELEMENT_SIZE * 8, ty - 25);
            killed += Integer.parseInt(pr.get("killed" + (i + 1)));
        }
        int ty = Constant.ELEMENT_SIZE * 6 + 3 * (Constant.ELEMENT_SIZE + 10);
        g.fillRect(Constant.ELEMENT_SIZE * 2 + 17, ty, Constant.FRAME_WIDTH * 2 / 3, 6);
        g.drawString("TOTAL  " + killed, Constant.ELEMENT_SIZE * 5 + 17, ty + 34);
        g.setColor(Color.decode("#636363"));
        g.setFont(font.deriveFont(10f));
        g.drawString("Click to continue", Constant.ELEMENT_SIZE * 3, Constant.ELEMENT_SIZE * 12 + 17);
    }
}
