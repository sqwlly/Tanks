package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.*;

import java.awt.*;

@IElement(speed = 1, width = Constant.ELEMENT_SIZE * 2, defense = 1000)
public class GameOver extends BaseElement implements IMovable, ITankCross, IBulletCross {
    private final Sprite sprite;

    public GameOver() {
        super(Constant.ELEMENT_SIZE * 6 - Constant.ELEMENT_SIZE * 1, Constant.ELEMENT_SIZE * 13);
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(4 * Constant.ELEMENT_SIZE, 4 * Constant.ELEMENT_SIZE,
                Constant.ELEMENT_SIZE * 2, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE * 2, Constant.ELEMENT_SIZE);
        sprite = new Sprite(sheet, 1, 0);
    }

    @Override
    public void action() {
        move();
    }

    @Override
    public void drawImage(Graphics g) {
        sprite.render(g, x, y, Constant.ELEMENT_SIZE * 4, Constant.ELEMENT_SIZE * 2);
    }

    @Override
    public void move() {
        if(y <= Constant.FRAME_HEIGHT / 2 - height) {
            return;
        }
        y -= speed.getValue();
    }
}
