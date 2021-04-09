package sample.auxiliary.state;

import sample.auxiliary.*;
import sample.content.player.Player;
import sample.content.substance.props.Prop;
import sample.content.substance.props.Props;

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

    public void generateProps() {
        Props p = Props.values()[CommonUtils.nextInt(0, Props.values().length)];
//        Props p = Props.Star;
        int tx = CommonUtils.nextInt(0, Constant.GAME_WIDTH - 34);
        int ty = CommonUtils.nextInt(0, Constant.GAME_HEIGHT - 34);
        new Prop(tx, ty, p);
    }

    public void action(Player player) {
//        System.out.println(player.getScore());
        if (player.getScore() >= map.getSumReward() && finishTime == 0) {
            finishTime = System.currentTimeMillis();
        }
        //清除完所有坦克即可进入下一关
        if(player.getScore() >= map.getSumReward() && System.currentTimeMillis() - finishTime > 3500) {
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
