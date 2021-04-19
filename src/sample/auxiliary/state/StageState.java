package sample.auxiliary.state;

import sample.auxiliary.*;

import java.awt.*;
import java.awt.event.MouseEvent;

public class StageState extends GameState{
    private GameStateManager gsm;
    private long time = System.nanoTime();
    private boolean choose;
    private int level_Id;

    public StageState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
        choose = true; //temp
    }

    @Override
    public void init() {
        time = System.currentTimeMillis();
        level_Id = Integer.parseInt(progress.get("levelToPlay"));
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
    public void stateAction() {
        if(Keys.ENTER.use()) {
            choose = false;
            progress.set("levelToPlay", level_Id + "");
            progress.store();
        }
        if(choose) {
            if(Keys.PLAY2_UP.use() && level_Id + 1 <= 18) {
                level_Id++;
            }else if(Keys.PLAY2_DOWN.use() && level_Id - 1 > 0) {
                level_Id--;
            }
        }
    }

    @Override
    public void drawImage(Graphics g) {
        g.setColor(Color.decode("#535353"));
        g.fillRect(0, 0, Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString("STAGE " + level_Id, Constant.FRAME_WIDTH / 2 - Constant.ELEMENT_SIZE * 2 + 10, Constant.FRAME_HEIGHT / 2 - 17);
        //让STAGE页面停留2.5秒钟
        if(System.currentTimeMillis() - time > 2000 && !choose) {
            gsm.setGameState(STATE.LEVEL);
        }
    }
}
