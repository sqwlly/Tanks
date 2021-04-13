package sample.auxiliary.state;

import sample.auxiliary.*;
import sample.auxiliary.service.EnemyElementService;
import sample.auxiliary.service.SubstanceElementService;
import sample.base.ElementService;
import sample.content.player.Player;
import sample.content.substance.EnemyIcon;
import sample.content.substance.P;
import sample.content.substance.PlayerIcon;
import sample.content.substance.props.Prop;
import sample.content.substance.props.Props;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class LevelState extends GameState implements ActionListener {
    private int Level_ID;
    private boolean init;

    public boolean isInit() {
        return init;
    }

    public Map getMap() {
        return map;
    }

    private Map map;
    private Timer timer = new Timer();
    private long finishTime;
    private PlayerIcon playerIcon;
    private Stack<EnemyIcon> enemyIcons;
    private P p_image;
    public LevelState(GameStateManager gsm) {
        ElementBean.init();
        this.gsm = gsm;
        init = false;
        init();
        action();
    }

    public void init() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                action();
            }
        };
        progress = Progress.getInstance();
        progress.set("restEnemy", "20");
        progress.set("currentScore", 0 + "");
        for(int i = 0; i < 4; ++i) {
            progress.set("killed" + (i + 1), 0 + "");
        }

        Level_ID = Integer.parseInt(progress.get("levelToPlay"));
        if(Level_ID >= 19) {
            setLevel_ID(Level_ID = 1);
        }
        int tx = Constant.GAME_WIDTH + 8;
        enemyIcons = new Stack<>();
        for(int i = 0; i < 10; ++i) {
            for(int j = 0; j < 2; ++j) {
                enemyIcons.add(new EnemyIcon(tx + 20 * j, Constant.ELEMENT_SIZE - 10 + 20 * i));
            }
        }
        p_image = new P(tx + 5, Constant.ELEMENT_SIZE * 10 - 17, 1);
        playerIcon = new PlayerIcon(tx - 3, Constant.ELEMENT_SIZE * 10 + 4);
        gsm.getPlayer().born();
        gsm.getHome().born();
        map = new Map("/levels/Level_" + Level_ID, gsm.getPlayer(), gsm.getHome());
        timer.schedule(timerTask, 0, 20);

        init = true;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(map.getHome().getHp().getValue());
        if(!map.getHome().getHp().health()) {
            //timer.cancel();
            try {
                Thread.sleep(200);
            }catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            gsm.setGameState(STATE.MENU);
        }
        System.out.println("level clicked");
    }

    @Override
    public void stateAction() {
    }

    public void wholeAction() {
        //玩家
        ElementService playerService = (ElementService) ElementBean.Player.getService();
        playerService.action(map.getPlayer());
        ElementService substanceService = (SubstanceElementService) ElementBean.Substance.getService();
        ElementService enemyService = (EnemyElementService) ElementBean.Enemy.getService();
        enemyService.action(map.getPlayer(), playerService);
        substanceService.action(map.getPlayer(), enemyService);
        substanceService.action(map.getPlayer(), playerService);
    }

    public void reduceEnemy() {
        enemyIcons.pop();
    }

    public void setLevel_ID(int level_ID) {
        progress.set("levelToPlay", level_ID + "");
        progress.store();
    }

    public void generateProps() {
        Props p = Props.values()[CommonUtils.nextInt(0, Props.values().length)];
//        Props p = Props.Spade;
        int tx = CommonUtils.nextInt(0, Constant.GAME_WIDTH - 34);
        int ty = CommonUtils.nextInt(0, Constant.GAME_HEIGHT - 34);
        new Prop(tx, ty, p);
    }

    public void action() {
        if(!init) return;
        //if(map == null) return; //这一句写的很烂，删了似乎会有Bug
        if(!map.getHome().getHp().health() && gsm.getPlayer().alive()) {
            map.getPlayer().die();
            System.out.println("boom");
            timer.cancel();
            Progress.getInstance().set("hearts", "0");
        }
        int restEnemy = Integer.parseInt(progress.get("restEnemy"));
        while (enemyIcons.size() - restEnemy > 0) {
            reduceEnemy();
        }
        int hearts = Integer.parseInt(progress.get("hearts"));
        if(hearts > 0) {

        }
//        System.out.println(player.getScore());
        if (map.getPlayer().getScore() >= map.getSumReward() && finishTime == 0) {
            finishTime = System.currentTimeMillis();
        }
        //清除完所有坦克即可进入下一关
        if(map.getPlayer().getScore() >= map.getSumReward() && System.currentTimeMillis() - finishTime > 3500) {
            map.getPlayer().initScore();
            gsm.setGameState(STATE.COUNT);
            setLevel_ID(Level_ID + 1);
        }
    }

    @Override
    public void drawImage(Graphics g) {
        g.setColor(Color.decode("#636363"));
        g.fillRect(Constant.FRAME_WIDTH - Constant.ELEMENT_SIZE * 2, 0, Constant.ELEMENT_SIZE * 2, Constant.FRAME_HEIGHT);
        playerIcon.drawImage(g);
        p_image.drawImage(g);
        g.setColor(Color.BLACK);
        g.setFont(font.deriveFont(18f));
        g.drawString(progress.get("hearts"), playerIcon.getX() + 28, playerIcon.getY() + 23);
        //暂时就先用着ArrayList吧
        for(int i = 0; i < enemyIcons.size(); ++i) {
            enemyIcons.get(i).drawImage(g);
        }
        for(ElementBean bean : ElementBean.values()) {
            bean.getService().drawImage(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
