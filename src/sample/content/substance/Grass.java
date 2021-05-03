package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IBulletCross;
import sample.base.IElement;
import sample.base.ITankCross;
import sample.content.common.Attribute;

import java.awt.*;
import java.awt.image.BufferedImage;

@IElement(width = Constant.ELEMENT_SIZE / 2 + 2, height = Constant.ELEMENT_SIZE / 2 + 2, defense = 100, hp = 1)
public class Grass extends BaseElement implements IBulletCross, ITankCross {
    private final Sprite sprite;

    public Grass(int x, int y) {
        super(x, y);
        sprite = new Sprite(new SpriteSheet(TextureAtlas.cut(4 * Constant.ELEMENT_SIZE, 7 * Constant.ELEMENT_SIZE,
                Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE / 2, Constant.ELEMENT_SIZE / 2),
                1, 0);
    }

    @Override
    public void drawImage(Graphics g) {
        sprite.render(g, x, y, Constant.ELEMENT_SIZE / 2 + 2, Constant.ELEMENT_SIZE / 2 + 2);
    }
}
