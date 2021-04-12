package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IElement;

import java.awt.*;

@IElement
public class P extends BaseElement {
    private final int type;
    private final Sprite sprite1, sprite2;

    public P(int x, int y, int type) {
        super(x, y);
        this.type = type;
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(2 * Constant.ELEMENT_SIZE, 4 * Constant.ELEMENT_SIZE,
                Constant.ELEMENT_SIZE * 2, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        sprite1 = new Sprite(sheet, 1, 0);
        sprite2 = new Sprite(sheet, 1, 1);
    }

    @Override
    public void drawImage(Graphics g) {
        if (type == 1) {
            sprite1.render(g, x, y);
        } else {
            sprite2.render(g, x, y);
        }
    }
}
