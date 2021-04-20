package sample.auxiliary;

import sample.auxiliary.state.*;
import sample.base.IDraw;
import sample.content.common.Tank;
import sample.content.player.Player;
import sample.content.substance.Home;

import java.awt.*;
import java.awt.event.MouseEvent;

public class GameStateManager implements IDraw {
    private final static int EAGLE_X = 6 * Constant.ELEMENT_SIZE, EAGLE_Y = 12 * Constant.ELEMENT_SIZE;

    private GameState gameState;
    private final Player player;

    public Home getHome() {
        return home;
    }

    private Home home;
    public GameStateManager() {
        player = new Player(4 * 34, 12 * 34);
        home = new Home(EAGLE_X, EAGLE_Y);
        setGameState(STATE.MENU);
        action();
    }

    public void mouseClicked(MouseEvent e){
        gameState.mouseClicked(e);
    }

    public Player getPlayer() {
        return player;
    }

    public void action() {
        CommonUtils.task(130, () -> {
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
        CommonUtils.task(20000, () -> {
            if (gameState instanceof LevelState) {
                ((LevelState) gameState).generateProps();
            }
        });
        //按时间周期生成敌方坦克
        CommonUtils.task(5500, () -> {
            int cnt = (int) ElementBean.Enemy.getService().getElementList().stream().filter(e -> e instanceof Tank).count();
            //为了减小游戏难度，场上敌方坦克数量不能超过四个
            if(cnt < 4) {
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
        }
    }

    @Override
    public void drawImage(Graphics g) {
        gameState.drawImage(g);
    }
}
