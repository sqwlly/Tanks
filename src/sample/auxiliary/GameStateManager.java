package sample.auxiliary;

import sample.auxiliary.state.*;
import sample.base.IDraw;
import sample.content.common.Tank;
import sample.content.player.Player;
import sample.content.player.Player_II;
import sample.content.substance.Home;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GameStateManager implements IDraw {
    private final static int EAGLE_X = 6 * Constant.ELEMENT_SIZE, EAGLE_Y = 12 * Constant.ELEMENT_SIZE;

    private GameState gameState;
    private final ArrayList<Player> players = new ArrayList<>();

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Home getHome() {
        return home;
    }

    private final Home home;

    public GameStateManager() {
        players.add(new Player(4 * 34, 12 * 34));
        players.add(new Player_II(8 * 34, 12 * 34));
        home = new Home(EAGLE_X, EAGLE_Y);
//        setGameState(STATE.COUNT);
        setGameState(STATE.MENU);
        action();
    }

    public void mouseClicked(MouseEvent e){
        gameState.mouseClicked(e);
    }

    public void action() {
        CommonUtils.task(100, () -> {
            gameState.stateAction();
        });
        //刷新动作内容
        CommonUtils.task(20, () -> {
            if(gameState instanceof LevelState &&
                    ((LevelState) gameState).isInit()) {
                ((LevelState) gameState).wholeAction();
            }
        });

        //按周期生成道具
        CommonUtils.task(13000, () -> {
            if (gameState instanceof LevelState) {
                ((LevelState) gameState).generateProps();
            }
        });
        //按时间周期生成敌方坦克
        CommonUtils.task(3500, () -> {
            int cnt = (int) ElementBean.Enemy.getService().getElementList().stream().filter(e -> e instanceof Tank).count();
            //为了减小游戏难度，场上敌方坦克数量不能超过四个 * playerNum
            int playerNum = Integer.parseInt(Progress.getInstance().get("playerNum"));
            if(cnt < 4 * playerNum) {
                if (gameState instanceof LevelState) {
                    ((LevelState) gameState).getMap().enemyBorn();
                }
                if (gameState instanceof LevelState) {
                    ((LevelState) gameState).reduceEnemyIcon();
                }
            }
        });
    }

    public void setGameState(STATE state) {
        switch (state) {
            case LEVEL:
                gameState = new LevelState(this);
                break;
            case STAGE:
                gameState = new StageState(this);
                break;
            case COUNT:
                gameState = new SettleScoreState(this);
                break;
            case MENU:
                gameState = new MenuState(this);
                break;
            case CONSTRUCTION:
                gameState = new Construction(this);
        }
    }

    @Override
    public void drawImage(Graphics g) {
        gameState.drawImage(g);
    }
}
