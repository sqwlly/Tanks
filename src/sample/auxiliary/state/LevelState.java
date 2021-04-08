package sample.auxiliary.state;

import sample.auxiliary.*;
import sample.auxiliary.service.EnemyElementService;
import sample.auxiliary.service.SubstanceElementService;
import sample.base.ElementService;
import sample.content.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class LevelState extends GameState implements ActionListener {
    private int Level_ID;
    public Map map;
    private Timer timer = new Timer(2500, this);
    private long finishTime;
    public LevelState(GameStateManager gsm) {
        ElementBean.init();
        this.gsm = gsm;
        init();
        timer.start();
    }

    public void init() {
        progress = Progress.getInstance();
        progress.set("currentScore", 0 + "");
        for(int i = 0; i < 4; ++i) {
            progress.set("killed" + (i + 1), 0 + "");
        }

        Level_ID = Integer.parseInt(progress.get("levelToPlay"));
        if(Level_ID >= 19) {
            setLevel_ID(Level_ID = 1);
        }
        map = new Map("/levels/Level_" + Level_ID, gsm.getPlayer());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("level clicked");
    }

    public void setLevel_ID(int level_ID) {
        progress.set("levelToPlay", level_ID + "");
        progress.store();
    }

    public void action(Player player) {
//        System.out.println(player.getScore());
        if (player.getScore() >= 100 && finishTime == 0) {
            finishTime = System.currentTimeMillis();
        }
        //清除完所有坦克即可进入下一关
        if(player.getScore() >= 100 && System.currentTimeMillis() - finishTime > 3500) {
            player.initScore();
            gsm.setGameState(STATE.COUNT);
            setLevel_ID(Level_ID + 1);
        }
    }

    @Override
    public void drawImage(Graphics g) {
        g.setColor(Color.decode("#636363"));
        g.fillRect(Constant.FRAME_WIDTH - Constant.ELEMENT_SIZE * 2, 0, Constant.ELEMENT_SIZE * 2, Constant.FRAME_HEIGHT);
        for(ElementBean bean : ElementBean.values()) {
            bean.getService().drawImage(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
