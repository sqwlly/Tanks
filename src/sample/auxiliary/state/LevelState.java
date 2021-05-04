package sample.auxiliary.state;

import sample.auxiliary.*;
import sample.auxiliary.Map;
import sample.auxiliary.audio.Audio;
import sample.auxiliary.service.EnemyElementService;
import sample.auxiliary.service.PlayerElementService;
import sample.auxiliary.service.SubstanceElementService;
import sample.base.ElementService;
import sample.content.common.Tank;
import sample.content.enemy.Enemy;
import sample.content.enemy.EnemyMode;
import sample.content.enemy.EnemyState;
import sample.content.enemy.IntelligentAI;
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
import java.util.*;

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
    private final Timer 
            timer = new Timer();
    private long finishTime;
    private PlayerIcon playerIcon;
    private Stack<EnemyIcon> enemyIcons;
    private P p_image;

    private IntelligentAI intelligentAI;

    public LevelState(GameStateManager gsm) {
        ElementBean.init();
        this.gsm = gsm;
        init = false;
        init();
    }

    public void init() {
        finishTime = 0;
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
        int playerNum = Integer.parseInt(progress.get("playerNum"));
        for(Player player : gsm.getPlayers()) {
            if(playerNum <= 0) break;
            playerNum--;
            System.out.println(playerNum);
            player.born();
            player.setBornTime(0);
            player.initScore();
            ElementBean.Player.getService().add(player);
        }
        gsm.getHome().born();
        map = new Map("/levels/Level_" + Level_ID, gsm);
        timer.schedule(timerTask, 0, 20);
        init = true;
        intelligentAI = new IntelligentAI(map, gsm);
        Audio.stage_start.play();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!gsm.getHome().getHp().health()) {
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
        int restEnemy = Integer.parseInt(progress.get("restEnemy"));
        if(restEnemy < 21) {
            ElementBean.Enemy.getService().getElementList().forEach(e -> {
                if (e instanceof Tank) {
                    Enemy enemy = (Enemy) e;
                    intelligentAI.bfs(enemy);
                }
            });
        }
        Level_ID = Integer.parseInt(progress.get("levelToPlay"));
    }

    public void wholeAction() {
        //玩家
        PlayerElementService playerService = (PlayerElementService) ElementBean.Player.getService();
        SubstanceElementService substanceService = (SubstanceElementService) ElementBean.Substance.getService();
        EnemyElementService enemyService = (EnemyElementService) ElementBean.Enemy.getService();
        substanceService.action(gsm, enemyService, playerService);
        enemyService.action(gsm, playerService);
        playerService.action(gsm);
    }

    public void reduceEnemyIcon() {
        if(enemyIcons.size() > 0) {
            enemyIcons.pop();
        }
    }

    public void setLevel_ID(int level_ID) {
        progress.set("levelToPlay", level_ID + "");
        progress.store();
    }

    public void generateProps() {
        //每个道具出现的概率
        int[] base = new int[] {15, 5, 15, 30, 10, 15, 10};
        int got = CommonUtils.nextInt(0, 100);
        ArrayList<Props> props = new ArrayList<>();
        for(int i = 0; i < base.length; ++i) {
            for(int j = 0; j < base[i]; ++j) {
                props.add(Props.values()[i]);
            }
        }
        //要打乱数组
        Collections.shuffle(props);
//        Props p = Props.values()[CommonUtils.nextInt(0, Props.values().length)];
//        Props p = Props.Bomb;
        Props p = props.get(got);
        int tx = CommonUtils.nextInt(0, Constant.GAME_WIDTH - 34);
        int ty = CommonUtils.nextInt(0, Constant.GAME_HEIGHT - 34);
        new Prop(tx, ty, p);
    }

    public void action() {
        if(!init) return;

        int hearts = Integer.parseInt(Progress.getInstance().get("hearts"));
        if(hearts < 0) {
            gsm.getHome().die();
        }

        for(Player player : gsm.getPlayers()) {
            if(!player.alive() && player.getBornTime() == 0) {
                System.out.println("player die");
                player.setBornTime(System.currentTimeMillis());
            }
            //ok, problem solved, use a bornTime var!
            //控制玩家在1.5s内复活
            if(!player.alive() && System.currentTimeMillis() - player.getBornTime() > 1500) {
                if(hearts > 0) {
                    player.born();
                    player.initLevel();
                    player.setBornTime(0);
                    ElementBean.Player.getService().add(player);
                    hearts--;
                }else{
                    gsm.getHome().die();
                }
                System.out.println(hearts);
                Progress.getInstance().set("hearts", hearts + "");

            }
            ElementBean.Player.getService().getElementList().forEach(e -> {
                //必须是服务列表里的player
                if(e == player) {
                    //老鹰没了玩家也得die
                    if(!gsm.getHome().getHp().health()) {
                        if(player.alive()) {
                            player.die();
                            System.out.println("player die");
                        }
                        Progress.getInstance().set("hearts", "0");

                        //gsm.setGameState(STATE.COUNT);
                    }
                }
            });

            int restEnemy = Integer.parseInt(progress.get("restEnemy"));
            if (player.getScore() >= map.getSumReward() && restEnemy == 0 && finishTime == 0) {
                finishTime = System.currentTimeMillis();
            }
            //清除完所有坦克即可进入下一关
            if(player.getScore() >= map.getSumReward() && restEnemy == 0 && System.currentTimeMillis() - finishTime > 2000) {
                System.out.println(player.getScore() + " " + map.getSumReward());
                player.initScore();
                gsm.setGameState(STATE.COUNT);
            }
        }
        if(!gsm.getHome().getHp().health()) {
            timer.cancel();
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
