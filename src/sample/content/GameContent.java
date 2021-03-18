package sample.content;

import sample.auxiliary.CommonUtils;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.IDraw;
import sample.content.player.Player;

import java.awt.*;

public class GameContent implements IDraw {
    Player player; //玩家

    public GameContent(TextureAtlas textureAtlas) {
        player =  new Player(textureAtlas);
        //刷新动作内容
        CommonUtils.task(25, () -> {
            player.action();//玩家动作
            this.wholeAction(player);//游戏整体动作
           // ElementBean.getGravity().universalGravitation();//万有引力
        });
    }

    public void buildEnemy() {

    }

    public void wholeAction(Player player) {

    }

    @Override
    public void drawImage(Graphics g) {
        player.drawImage(g);
    }
}
