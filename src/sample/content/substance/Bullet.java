package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.auxiliary.ElementBean;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.*;
import sample.content.common.Attribute;
import sample.content.common.Tank;

import java.awt.*;
import java.util.HashMap;

@IElement(width = 9, height = 10, speed = 7, attack = 50)
public class Bullet extends BaseElement implements IMovable {

    private final HashMap<Direction, Sprite> spriteMap;

    private final Tank from;

    private boolean hitTarget;

    public boolean hitTarget() {
        return hitTarget;
    }

    public void hit() {
        this.hitTarget = true;
    }

    public Bullet(int x, int y, Direction direction, Tank from) {
        super(x, y);
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
            speed.setValue(speed.getValue() * 2);
        }
        if(level == 2) {
            attack.add(50);
        }else if(level == 3) {
            attack.add(100);
        }
    }

    public Tank getFrom() {
        return from;
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
        if(from.getCurrentBulletNum() + 1 <= from.getBulletNum()) {
            from.bulletNumAdd();
        }
    }

    @Override
    public boolean remove(BaseElement element) {
        if(!alive() || encounterSide() || hitTarget()) {
            die();
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
