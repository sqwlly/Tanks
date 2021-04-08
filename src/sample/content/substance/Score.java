package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.ElementBean;
import sample.auxiliary.graphics.Animation;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IBulletCross;
import sample.base.IElement;
import sample.base.ITankCross;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@IElement
public class Score extends BaseElement implements IBulletCross, ITankCross {
    private int type;
    private final List<Animation> animations = new ArrayList<>();
    public Score(int x, int y, int type) {
        super(x, y);
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(27 * Constant.ELEMENT_SIZE, 5 * Constant.ELEMENT_SIZE,
                Constant.ELEMENT_SIZE * 5, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        for(int i = 0; i < 5; ++i) {
            List<Sprite> sprites = new ArrayList<>();
            sprites.add(new Sprite(sheet, 1, i));
            Animation animation = new Animation(new BufferedImage[]{sheet.getSprite(i), sheet.getSprite(i)}, 50);
            animation.start();
            animations.add(animation);
        }
        this.type = type;
    }

    @Override
    public void drawImage(Graphics g) {
        if(animations.get(type).hasPlayed(5)) {
            ElementBean.Substance.getService().remove(this);
            return;
        }
        animations.get(type).update();
        g.drawImage(animations.get(type).getSprite(), x, y, width, height, null);
    }
}
