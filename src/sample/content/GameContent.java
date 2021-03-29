package sample.content;

import sample.auxiliary.CommonUtils;
import sample.auxiliary.Constant;
import sample.auxiliary.ElementBean;
import sample.auxiliary.Map;
import sample.auxiliary.service.EnemyElementService;
import sample.auxiliary.service.SubstanceElementService;
import sample.base.ElementService;
import sample.base.IDraw;
import sample.content.player.Player;

import java.awt.*;

public class GameContent implements IDraw {
    private Map map;
    public GameContent(Map map) {
        //刷新动作内容
        this.map = map;
        CommonUtils.task(20, () -> {
            this.wholeAction(map.getPlayer());//游戏整体动作
//            player.action();//玩家动作

        });
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
    }

    @Override
    public void drawImage(Graphics g) {
        for(ElementBean bean : ElementBean.values()) {
            bean.getService().drawImage(g);
        }
    }
}
