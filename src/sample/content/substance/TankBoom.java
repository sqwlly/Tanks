package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.ElementBean;
import sample.auxiliary.graphics.Animation;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IBulletCross;
import sample.base.IElement;
import sample.base.ITankCross;

import java.awt.*;
import java.awt.image.BufferedImage;

@IElement(width = Constant.ELEMENT_SIZE * 2, height = Constant.ELEMENT_SIZE * 2, defense = 1000)
public class TankBoom extends BaseElement implements ITankCross, IBulletCross {

    private final Animation animation;

    public TankBoom(int x, int y) {
        super(x, y);
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(23 * Constant.ELEMENT_SIZE, 4 * Constant.ELEMENT_SIZE,
                Constant.ELEMENT_SIZE * 4, Constant.ELEMENT_SIZE * 2),
                Constant.ELEMENT_SIZE * 2, Constant.ELEMENT_SIZE * 2);
        BufferedImage[] act = new BufferedImage[2];
        for(int i = 0; i < 2; ++i) {
            act[i] = sheet.getSprite(i);
        }
        animation = new Animation(act, 75);
        animation.start();
    }

    @Override
    public void drawImage(Graphics g) {
        if(animation.hasPlayed(3)) {
            ElementBean.Substance.getService().remove(this);
            return;
        }
        g.drawImage(animation.getSprite(), x, y, width, height, null);
        animation.update();
    }
}
