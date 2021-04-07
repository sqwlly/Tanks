package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.ElementBean;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IElement;

import java.awt.*;

@IElement
public class Home extends BaseElement {

    private Sprite alive, dead;

    private GameOver gameOver;
    public Home(int x, int y) {
        super(x, y);
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(19 * Constant.ELEMENT_SIZE, 5 * Constant.ELEMENT_SIZE,
                Constant.ELEMENT_SIZE * 2, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        alive = new Sprite(sheet, 1, 0);
        dead = new Sprite(sheet, 1, 1);
    }

    @Override
    public void action() {
        if(!alive() && gameOver == null) {
            gameOver = new GameOver();
            ElementBean.Substance.getService().add(gameOver);
        }
    }

    @Override
    public void drawImage(Graphics g) {
        if(alive()) {
            alive.render(g, x, y);
        }else{
            dead.render(g, x, y);
        }
    }
}
