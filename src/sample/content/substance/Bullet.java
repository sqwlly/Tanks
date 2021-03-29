package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.auxiliary.ElementBean;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IBulletCross;
import sample.base.IElement;

import java.awt.*;
import java.util.HashMap;

@IElement(width = 9, height = 10, speed = 6)
public class Bullet extends BaseElement implements IBulletCross {

    private HashMap<Direction, Sprite> spriteMap;

    public boolean isDestroy_Steel() {
        return destroy_Steel;
    }

    public void setDestroy_Steel(boolean destroy_Steel) {
        this.destroy_Steel = destroy_Steel;
    }

    private boolean destroy_Steel;

    public Bullet(int x, int y, Direction direction) {
        super(x, y);
        this.direction = direction;
        spriteMap = new HashMap<>();
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(0 * Constant.ELEMENT_SIZE, 5 * Constant.ELEMENT_SIZE,
                20, 20), 10);
//        int i = 0;
        spriteMap.put(Direction.UP, new Sprite(sheet, 1, 0));
        spriteMap.put(Direction.DOWN, new Sprite(sheet, 1, 1));
        spriteMap.put(Direction.LEFT, new Sprite(sheet, 1, 2));
        spriteMap.put(Direction.RIGHT, new Sprite(sheet, 1, 3));
        destroy_Steel = false;
    }

    @Override
    public void action() {
        super.move();
    }

    public void boom() {
        //die();
        ElementBean.Substance.getService().add(new BulletBoom(x - 17 + 5, y - 17 + 5));
        //ElementBean.Player.getService().remove(this);
        //this.setX(-Constant.FRAME_WIDTH);
        //die();
    }

    @Override
    public boolean remove(BaseElement element) {
        if(!alive() || encounterSide()) {
            boom();
            return true;
        }
        return super.remove(element);
    }

    @Override
    public void drawImage(Graphics g) {
        if(!alive()) return;
        spriteMap.get(direction).render(g, x, y);
    }
}
