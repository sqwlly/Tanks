package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IElement;
import sample.content.common.Attribute;
import sample.content.player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

@IElement(width = Constant.ELEMENT_SIZE / 2, height = Constant.ELEMENT_SIZE / 2, defense = 100, hp = 100)
public class Steel extends BaseElement {
    //    private int[] state;
    private Sprite sprite;

    public Steel(int x, int y) {
        super(x, y);
        defense = new Attribute(100);
        BufferedImage[] act = new BufferedImage[4];
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(0, 6 * Constant.ELEMENT_SIZE,
                Constant.ELEMENT_SIZE * 4, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
//        state = new int[] {1, 1, 1, 1};
        sprite = new Sprite(sheet, 1, 0);
    }

    @Override
    public void drawImage(Graphics g) {
        sprite.render(g, x, y, Constant.ELEMENT_SIZE + 1, Constant.ELEMENT_SIZE + 1);
    }
}
