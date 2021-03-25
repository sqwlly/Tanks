package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.graphics.Animation;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IBulletCross;
import sample.base.IElement;
import sample.base.ITankCross;
import sample.content.player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @Description 此类可以让坦克处于无敌状态
 */
@IElement
public class Invincible extends BaseElement implements IBulletCross, ITankCross {

    private Animation animation;

    public Invincible(int x, int y) {
        super(x, y);
        BufferedImage[] act = new BufferedImage[3];
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(13 * Constant.ELEMENT_SIZE, 7 * Constant.ELEMENT_SIZE,
                Constant.ELEMENT_SIZE * 3, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE);
        act[0] = sheet.getSprite(0);
        act[1] = sheet.getSprite(2);
        animation = new Animation(act, 35);
        animation.start();
    }

    public void movedByPlayer(Player player) {
        this.setX(player.getX());
        this.setY(player.getY());
    }

    @Override
    public void drawImage(Graphics g) {
        if(animation.hasPlayed(50)) {
            return;
        }
        g.drawImage(animation.getSprite(), x, y, Constant.ELEMENT_SIZE - 3, Constant.ELEMENT_SIZE - 3, null);
        animation.update();
    }
}
