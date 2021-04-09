package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.auxiliary.ElementBean;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.*;
import sample.content.common.Attribute;

import java.awt.*;
import java.util.HashMap;

@IElement(width = 9, height = 10, speed = 5, attack = 51)
public class Bullet extends BaseElement implements IMovable {

    private final HashMap<Direction, Sprite> spriteMap;

    private final Tank from;

    public Bullet(int x, int y, Direction direction, Tank from) {
        super(x, y);
        attack = new Attribute(51);
        this.from = from;
        this.direction = direction;
        spriteMap = new HashMap<>();
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(0 * Constant.ELEMENT_SIZE, 5 * Constant.ELEMENT_SIZE,
                20, 20), 10, 10);
        spriteMap.put(Direction.UP, new Sprite(sheet, 1, 0));
        spriteMap.put(Direction.DOWN, new Sprite(sheet, 1, 1));
        spriteMap.put(Direction.LEFT, new Sprite(sheet, 1, 2));
        spriteMap.put(Direction.RIGHT, new Sprite(sheet, 1, 3));
        this.getHp().setSubBaseValue(100);
    }

    public void setLevel(int level) {
        if(level >= 1) {
            speed.setValue(speed.getValue() * 3 / 2);
        }
        if(level == 2) {
            attack.add(51);
        }else if(level == 3) {
            attack.add(101);
        }
    }

    @Override
    public void action() {
        move();
    }

    @Override
    public void move() {
        if(direction.up()) {
            this.y -= speed.getValue();
        }else if(direction.down()) {
            this.y += speed.getValue();
        }else if(direction.right()) {
            this.x += speed.getValue();
        }else if(direction.left()) {
            this.x -= speed.getValue();
        }
    }

    @Override
    public void die() {
        this.hp.setValue(0);
        boom();
    }

    public void boom() {
        ElementBean.Substance.getService().add(new BulletBoom(x - 17 + 5, y - 17 + 5));
        if(!from.fireAble()) {
            from.bulletNumAdd();
        }
    }

    @Override
    public boolean remove(BaseElement element) {
        if(!alive() || encounterSide()) {
            boom();
//            System.out.println("bullet remove");
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
