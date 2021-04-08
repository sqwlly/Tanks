package sample.auxiliary.state;

import sample.auxiliary.Constant;
import sample.auxiliary.GameStateManager;
import sample.auxiliary.Progress;
import sample.auxiliary.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StageState extends GameState{
    private GameStateManager gsm;
    private Font font;
    private Progress pr;
    private long time = System.nanoTime();
    public StageState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    @Override
    public void init() {
        try {
            //加载字体
            font = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.loadFontStream("joystix.ttf")).deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        pr = Progress.getInstance();
        time = System.currentTimeMillis();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int c = e.getButton();
        System.out.println("clicked");
        if(c == MouseEvent.BUTTON1) {
            gsm.setGameState(STATE.LEVEL);
        }
    }

    @Override
    public void drawImage(Graphics g) {
        g.setColor(Color.decode("#535353"));
        g.fillRect(0, 0, Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString("STAGE " + pr.get("levelToPlay"), Constant.FRAME_WIDTH / 2 - Constant.ELEMENT_SIZE * 2 + 10, Constant.FRAME_HEIGHT / 2 - 17);
        //让STAGE页面停留2.5秒钟
        if(System.currentTimeMillis() - time > 2500) {
            gsm.setGameState(STATE.LEVEL);
        }
    }
}
