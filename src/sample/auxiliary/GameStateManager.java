package sample.auxiliary;

import sample.auxiliary.state.*;
import sample.base.IDraw;

import java.awt.*;
import java.awt.event.MouseEvent;

public class GameStateManager implements IDraw {

    private GameState gameState;
    private Progress progress;
    public GameStateManager() {
        progress = Progress.getInstance();
        setGameState(STATE.LEVEL);
        action();
    }

    public void mouseClicked(MouseEvent e){
        gameState.mouseClicked(e);
    }

    public void action() {
        //这一段代码写的不是很好
        if (gameState instanceof LevelState) {
            //刷新动作内容
            CommonUtils.task(20, () -> {
                if (gameState instanceof LevelState) {
                    ((LevelState) gameState).wholeAction(((LevelState) gameState).map.getPlayer());//游戏整体动作
                }
            });
            //按时间周期生成敌方坦克
            CommonUtils.task(10000, () -> {
                if (gameState instanceof LevelState) {
                    ((LevelState) gameState).map.enemyBorn();
                }
            });
        }
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
                gameState = new MenuState();
                break;
        }
    }

    @Override
    public void drawImage(Graphics g) {
        gameState.drawImage(g);
    }
}
