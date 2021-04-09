package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.graphics.Animation;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IBulletCross;
import sample.base.IElement;
import sample.base.ITankCross;

import java.awt.*;
import java.awt.image.BufferedImage;

@IElement(defense = 1000)
public class Born extends BaseElement implements IBulletCross, ITankCross {
    private final Animation animation;

    public Born(int x, int y) {
        super(x, y);
        BufferedImage[] act = new BufferedImage[4];
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(16 * Constant.ELEMENT_SIZE, 4 * Constant.ELEMENT_SIZE,
                Constant.ELEMENT_SIZE * 4, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        for(int i = 0; i < 4; ++i) {
            act[i] = sheet.getSprite(i);
        }
        animation = new Animation(act, 60);
        animation.start();
    }

    @Override
    public void drawImage(Graphics g) {
        if(animation.hasPlayed(5)) {
            die();
            return;
        }
        g.drawImage(animation.getSprite(), x, y, null);
        animation.update();
    }
}
