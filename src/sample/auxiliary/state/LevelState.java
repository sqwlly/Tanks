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
        Level_ID = Integer.parseInt(progress.get("levelToPlay"));
        if(Level_ID >= 19) {
            setLevel_ID(Level_ID = 1);
        }
        map = new Map("/levels/Level_" + Level_ID);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("level clicked");
    }

    public void setLevel_ID(int level_ID) {
        progress.set("levelToPlay", level_ID + "");
        progress.store();
    }

    public void wholeAction(Player player) {
        //玩家
        ElementService playerService = (ElementService) ElementBean.Player.getService();
        ElementService substanceService = (SubstanceElementService) ElementBean.Substance.getService();
        ElementService enemyService = (EnemyElementService) ElementBean.Enemy.getService();
        enemyService.action(player, playerService);
        substanceService.action(player, enemyService);
        substanceService.action(player, playerService);
        playerService.action(player);

        //暂时就先写在这里吧。
        //System.out.println(player.getScore());
        if (player.getScore() >= 100 && finishTime == 0) {
            finishTime = System.currentTimeMillis();
            player.initScore();
        }
        if(player.getScore() >= 100 && System.currentTimeMillis() - finishTime > 5000) {
            setLevel_ID(Level_ID + 1);
            player.initScore();
            gsm.setGameState(STATE.COUNT);
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
