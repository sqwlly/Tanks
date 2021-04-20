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
@IElement(defense = 1000)
public class Invincible extends BaseElement implements IBulletCross, ITankCross {

    private final Animation animation;
    private Born born;
    public Invincible(int x, int y, Born born) {
        super(x, y);
        this.born = born;
        BufferedImage[] act = new BufferedImage[3];
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(13 * Constant.ELEMENT_SIZE, 7 * Constant.ELEMENT_SIZE,
                Constant.ELEMENT_SIZE * 3, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        act[0] = sheet.getSprite(0);
        act[1] = sheet.getSprite(2);
        animation = new Animation(act, 15);
        animation.start();
    }

    public void movedByPlayer(Player player) {
        this.setX(player.getX());
        this.setY(player.getY());
    }

    @Override
    public boolean remove(BaseElement element) {
        if(element instanceof Player && (!element.alive())) {
            return true;
        }
        return super.remove(element);
    }

    @Override
    public void drawImage(Graphics g) {
        if(!born.isComplete()) return;
        if(animation.hasPlayed(200)) {
            die();
            return;
        }
        g.drawImage(animation.getSprite(), x, y, Constant.ELEMENT_SIZE - 2, Constant.ELEMENT_SIZE - 2, null);
        animation.update();
    }
}
