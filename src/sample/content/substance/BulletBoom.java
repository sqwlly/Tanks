package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.ElementBean;
import sample.auxiliary.audio.Audio;
import sample.auxiliary.audio.MediaPlayer;
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

@IElement(defense = 1000)
public class BulletBoom extends BaseElement implements IBulletCross, ITankCross {

    private final Animation animation;

    public BulletBoom(int x, int y) {
        super(x, y);
        BufferedImage[] act = new BufferedImage[3];
        List<Sprite> sprites = new ArrayList<>();
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(20 * Constant.ELEMENT_SIZE, 4 * Constant.ELEMENT_SIZE,
                Constant.ELEMENT_SIZE * 3, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        for(int i = 0; i < 3; ++i) {
            act[i] = sheet.getSprite(i);
            sprites.add(new Sprite(sheet, 1, i));
        }
        animation = new Animation(act, 25);
        animation.start();
    }

    @Override
    public void drawImage(Graphics g) {
        if(animation.hasPlayed(1)) {
            ElementBean.Substance.getService().remove(this);
            return;
        }
        g.drawImage(animation.getSprite(), x, y, null);
        animation.update();
    }
}
