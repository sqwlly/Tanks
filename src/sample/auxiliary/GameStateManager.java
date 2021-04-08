package sample.auxiliary;

import sample.auxiliary.service.EnemyElementService;
import sample.auxiliary.service.SubstanceElementService;
import sample.auxiliary.state.*;
import sample.base.ElementService;
import sample.base.IDraw;
import sample.content.player.Player;

import java.awt.*;
import java.awt.event.MouseEvent;

public class GameStateManager implements IDraw {

    private GameState gameState;
    private Progress progress;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private Player player;

    public GameStateManager() {
        progress = Progress.getInstance();
        player = new Player(4 * Constant.ELEMENT_SIZE, 12 * Constant.ELEMENT_SIZE);
        setGameState(STATE.LEVEL);
        action();
        //playerInit();
    }

    public void playerInit() {
        ElementBean.Player.getService().add(player);
        ElementBean.Substance.getService().add(player.getBorn());
        ElementBean.Substance.getService().add(player.getInvincible());
    }

    public void mouseClicked(MouseEvent e){
        gameState.mouseClicked(e);
    }

    public void action() {
        if (gameState instanceof LevelState) {
            //刷新动作内容
            CommonUtils.task(20, () -> {
                this.wholeAction(player);
                if(gameState instanceof LevelState) {
                    ((LevelState) gameState).action(player);
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

    public void wholeAction(Player player) {
        //玩家
        ElementService playerService = (ElementService) ElementBean.Player.getService();
        playerService.action(player);
        ElementService substanceService = (SubstanceElementService) ElementBean.Substance.getService();
        ElementService enemyService = (EnemyElementService) ElementBean.Enemy.getService();
        enemyService.action(player, playerService);
        substanceService.action(player, enemyService);
        substanceService.action(player, playerService);
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
