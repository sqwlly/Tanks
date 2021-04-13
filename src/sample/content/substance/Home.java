package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.ElementBean;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IElement;

import java.awt.*;

@IElement(hp = 100)
public class Home extends BaseElement {

    private final Sprite alive, dead;

    private GameOver gameOver;
    public Home(int x, int y) {
        super(x, y);
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(19 * Constant.ELEMENT_SIZE, 5 * Constant.ELEMENT_SIZE,
                Constant.ELEMENT_SIZE * 2, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        alive = new Sprite(sheet, 1, 0);
        dead = new Sprite(sheet, 1, 1);
        hp.setMinValue(80);
    }

    @Override
    public boolean beforeActionJudge() {
        if(!hp.health()) {
            return false;
        }
        return super.beforeActionJudge();
    }

    @Override
    public void action() {
        if(!hp.health() && gameOver == null) {
            gameOver = new GameOver();
            ElementBean.Substance.getService().add(gameOver);
        }
    }

    public void born() {
        this.gameOver = null;
        this.defense.setValue(50);
        this.hp.setValue(100);
    }

    @Override
    public void drawImage(Graphics g) {
        if(hp.health()) {
            alive.render(g, x, y);
        }else{
            this.defense.setValue(1000);
            dead.render(g, x, y);
        }
    }
}
