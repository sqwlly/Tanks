package sample.content.substance.props;

import sample.auxiliary.Constant;
import sample.auxiliary.ElementBean;
import sample.auxiliary.graphics.Animation;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IBulletCross;
import sample.base.IElement;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

@IElement(defense = 1000)
public class Prop extends BaseElement implements IBulletCross {
    private final Props prop;
    private final Animation animation, nearDis;
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
        BufferedImage[] act = new BufferedImage[2];
        act[0] = act[1] = sheet.getSprite(propMap.get(prop));
        animation = new Animation(act, 305);
        animation.start();
        act[1] = sheet.getSprite(5);
        nearDis = new Animation(act, 200);
        nearDis.start();
        ElementBean.Substance.getService().add(this);
    }

    public Props getProp() {
        return prop;
    }

    public void action() {
        if(this.getProp() == Props.Spade) {

        }
    }

    @Override
    public void drawImage(Graphics g) {
        if(nearDis.hasPlayed(30)) {
            die();
            return;
        }
        if(animation.hasPlayed(40)) {
            //快要消失时闪烁状态
            g.drawImage(nearDis.getSprite(), x, y, width, height, null);
            nearDis.update();
            return;
        }
        g.drawImage(animation.getSprite(), x, y, width, height, null);
        animation.update();
    }
}
