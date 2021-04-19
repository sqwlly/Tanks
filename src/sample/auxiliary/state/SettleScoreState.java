package sample.auxiliary.state;

import sample.auxiliary.Constant;
import sample.auxiliary.GameStateManager;
import sample.auxiliary.Progress;
import sample.auxiliary.ResourceLoader;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.content.substance.Arrow;

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
    private final List<Sprite> sprites = new ArrayList<>();
    private final ArrayList<Arrow> arrow_left = new ArrayList<>();
    private int levelID;
    private final int[] killedNum = new int[6];
    private int total_killed = 0;
    private final boolean[] complete = new boolean[6];

    public SettleScoreState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    @Override
    public void init() {
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(0, Constant.ELEMENT_SIZE * 2,
                Constant.ELEMENT_SIZE * 32, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        for(int i = 0; i < 4; ++i) {
            sprites.add(new Sprite(sheet, 1, i * 8));
        }
        for(int i = 0; i < 4; ++i) {
            arrow_left.add(new Arrow(-1, -1, true));
        }
        levelID = Integer.parseInt(progress.get("levelToPlay"));
        for(int i = 0; i < 4; ++i) {
            total_killed += Integer.parseInt(progress.get("killed" + (i + 1)));
        }
        complete[0] = true;

        //最高分更新
        int curScore = Integer.parseInt(progress.get("currentScore"));
        int highScore = Integer.parseInt(progress.get("highScore"));
        if(curScore > highScore) {
            highScore = curScore;
        }
        progress.set("highScore", highScore + "");
        progress.store();
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

        //STAGE
        g.setFont(font.deriveFont(20f));
        g.setColor(Color.WHITE);
        g.drawString("STAGE " + levelID, Constant.ELEMENT_SIZE * 6 - 17, Constant.ELEMENT_SIZE * 3 / 2);

        //I-PLAYER
        g.setColor(Color.RED);
        g.drawString("I-PLAYER", Constant.ELEMENT_SIZE * 2 - 17, Constant.ELEMENT_SIZE * 3);

        //得分
        g.setColor(Color.YELLOW.darker());
        int tx = Constant.ELEMENT_SIZE * 3;
        g.drawString(progress.get("currentScore"), tx, Constant.ELEMENT_SIZE * 4);

        g.setColor(Color.WHITE);
        for (int i = 0; i < 4; i++) {
            int ty = Constant.ELEMENT_SIZE * 5 + i * (Constant.ELEMENT_SIZE + 10) + 10;
            g.drawString(Integer.parseInt(progress.get("killed" + (i + 1))) * (i + 1) + (Integer.parseInt(progress.get("killed" + (i + 1))) == 0 ? "" : "00"),
                    tx, ty);
            g.drawString("PTS", tx + Constant.ELEMENT_SIZE * 3, ty);
            int killed_i = Integer.parseInt(progress.get("killed" + (i + 1)));
            g.drawString(killedNum[i + 1] + "", tx + Constant.ELEMENT_SIZE * 6, ty);
            //如果前一个(上一行)数字已经完毕并且当前数字加一不会超限
            if(complete[i] && killedNum[i + 1] + 1 <= killed_i) {
                killedNum[i + 1]++;
            }
            //如果数字加到上限
            if(killedNum[i + 1] >= killed_i){
                complete[i + 1] = true;
            }

            //左箭头
            arrow_left.get(i).setX(tx + 6 * Constant.ELEMENT_SIZE + 24);
            arrow_left.get(i).setY(ty - 24);
            arrow_left.get(i).drawImage(g);

            //击杀掉的坦克icon类型
            sprites.get(i).render(g, tx + Constant.ELEMENT_SIZE * 8, ty - 25);

            try {
                //让数字动起来~
                Thread.sleep(30);
            }catch (Exception ignored) {

            }
        }
        int ty = Constant.ELEMENT_SIZE * 6 + 3 * (Constant.ELEMENT_SIZE + 10);
        //结算白线
        g.fillRect(Constant.ELEMENT_SIZE * 2 + 17, ty, Constant.FRAME_WIDTH * 2 / 3, 6);

        //总共击杀坦克数量
        g.drawString("TOTAL  " + killedNum[5], Constant.ELEMENT_SIZE * 5 + 17, ty + 34);
        if(complete[4] && killedNum[5] < total_killed){
            killedNum[5]++;
        }

        //click to continue~
        g.setColor(Color.decode("#636363"));
        g.setFont(font.deriveFont(10f));
        g.drawString("Click to continue", Constant.ELEMENT_SIZE * 3, Constant.ELEMENT_SIZE * 12 + 17);
    }
}
