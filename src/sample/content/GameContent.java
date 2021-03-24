package sample.content;

import sample.auxiliary.CommonUtils;
import sample.auxiliary.Constant;
import sample.auxiliary.ElementBean;
import sample.auxiliary.service.SubstanceElementService;
import sample.base.ElementService;
import sample.base.IDraw;
import sample.content.player.Player;
import sample.content.substance.Tile;

import java.awt.*;

public class GameContent implements IDraw {
    Player player; //玩家

    public GameContent() {
        player =  new Player(100, 100);
        ElementBean.Player.getService().add(player);
        ElementBean.Substance.getService().add(player.getBorn());
        buildTiles();
        //buildTiles();
        //刷新动作内容
        CommonUtils.task(25, () -> {
            this.wholeAction(player);//游戏整体动作
//            player.action();//玩家动作

        });
    }


    public void buildTiles() {
        for(int i = 0; i < 10; ++i) {
            Tile tile = new Tile(150 + i * Constant.ELEMENT_SIZE / 2, 150, 1);
            ElementBean.Substance.getService().add(tile);
        }
    }

    public void buildEnemy() {

    }

    public void wholeAction(Player player) {
        //玩家
        ElementService playerService = (ElementService) ElementBean.Player.getService();
        ElementService substanceService = (SubstanceElementService) ElementBean.Substance.getService();
        substanceService.action(player);
        playerService.action(player);
    }

    @Override
    public void drawImage(Graphics g) {
        for(ElementBean bean : ElementBean.values()) {
            bean.getService().drawImage(g);
        }
    }
}
