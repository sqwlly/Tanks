package sample.content.substance.props;

import javafx.util.Pair;
import sample.auxiliary.Constant;
import sample.auxiliary.ElementBean;
import sample.auxiliary.graphics.Animation;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IBulletCross;
import sample.base.IElement;
import sample.content.substance.Score;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

@IElement
public class Prop extends BaseElement implements IBulletCross {
    private final Props prop;
    private final Animation animation;
    public Prop(int x, int y, Props prop) {
        super(x, y);
        this.prop = prop;
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(12 * Constant.ELEMENT_SIZE, 6 * Constant.ELEMENT_SIZE,
                13 * Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        HashMap<Props, Integer> propMap = new HashMap<>();
        propMap.put(Props.Star, 4);
        propMap.put(Props.StopWatch, 2);
        propMap.put(Props.Gun, 3);
        propMap.put(Props.Iron_cap, 6);
        propMap.put(Props.Bomb, 8);
        propMap.put(Props.Spade, 10);
        propMap.put(Props.Tank, 12);
        BufferedImage[] act = new BufferedImage[]{sheet.getSprite(propMap.get(prop))};
        animation = new Animation(act, 505);
        animation.start();
        ElementBean.Substance.getService().add(this);
    }

    public Props getProp() {
        return prop;
    }

    @Override
    public void drawImage(Graphics g) {
        if(animation.hasPlayed(100)) {
            die();
            return;
        }
        g.drawImage(animation.getSprite(), x, y, width, height, null);
        animation.update();
    }
}
