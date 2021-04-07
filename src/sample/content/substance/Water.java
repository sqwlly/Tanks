package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.graphics.Animation;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IBulletCross;
import sample.base.IElement;

import java.awt.*;
import java.awt.image.BufferedImage;

@IElement(width = Constant.ELEMENT_SIZE / 2, height = Constant.ELEMENT_SIZE / 2)
public class Water extends BaseElement implements IBulletCross {
    private Animation animation;

    public Water(int x, int y) {
        super(x, y);
        BufferedImage[] act = new BufferedImage[2];
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(0, Constant.ELEMENT_SIZE * 7,
                Constant.ELEMENT_SIZE * 2, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        for(int i = 0; i < act.length; ++i) {
            act[i] = sheet.getSprite(i);
        }
        animation = new Animation(act, 200);
        animation.start();
    }

    @Override
    public void drawImage(Graphics g) {
        g.drawImage(animation.getSprite(), x, y, Constant.ELEMENT_SIZE / 2 + 2, Constant.ELEMENT_SIZE / 2 + 2, null);
        animation.update();
    }
}
