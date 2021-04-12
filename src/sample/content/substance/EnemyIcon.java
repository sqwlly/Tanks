package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IElement;

import java.awt.*;

@IElement
public class EnemyIcon extends BaseElement {
    private final Sprite sprite;
    public EnemyIcon(int x, int y) {
        super(x, y);
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(1 * Constant.ELEMENT_SIZE, 4 * Constant.ELEMENT_SIZE,
                Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        sprite = new Sprite(sheet, 1, 0);
    }

    @Override
    public void drawImage(Graphics g) {
        sprite.render(g, x, y);
    }
}
