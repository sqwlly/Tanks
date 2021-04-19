package sample.content.substance;

import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;

import java.awt.*;

public class Arrow extends BaseElement {
    private Sprite arrow_left, arrow_right;
    private boolean left;
    public Arrow(int x, int y, boolean left) {
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(6 * 34, 4 * 34, 2 * 34, 34),
                34, 34);
        arrow_left = new Sprite(sheet, 1, 0);
        arrow_right = new Sprite(sheet, 1, 1);
        this.left = left;
    }
    @Override
    public void drawImage(Graphics g) {
        if(left) {
            arrow_left.render(g, x, y);
        }else{
            arrow_right.render(g, x, y);
        }
    }
}
