package sample.content.player;

import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.auxiliary.ElementBean;
import sample.auxiliary.Keys;
import sample.auxiliary.graphics.Animation;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IElement;
import sample.content.substance.Born;
import sample.content.substance.Bullet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@IElement(width = Constant.ELEMENT_SIZE - 3, height = Constant.ELEMENT_SIZE - 3)
public class Player extends BaseElement {
    private final static int size = Constant.ELEMENT_SIZE;

    private final static float scale = 1f;

    private HashMap<Direction, Sprite> spriteMap;
    private Bullet bullet;
    private final Born born;
    private int oldX, oldY;

    public Player(int x, int y) {
        super(x, y);
        spriteMap = new HashMap<>();
        int cnt = 0;
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(0, 0, size * 8, size), size);
        for(Direction d : Direction.values()) {
            spriteMap.put(d, new Sprite(sheet, scale, cnt));
            cnt += 2;
        }
        born = new Born(x, y);
        bullet = new Bullet(x, y, direction);
        bullet.die();
    }

    public void beHurt() {

    }

    @Override
    public void action() {
        move();
        if(Keys.SPACE.use()) {
            shoot();
        }
    }

    public Bullet getBullet() {
        return bullet;
    }

    public Born getBorn() {
        return born;
    }

    public void shoot() {
        if(bullet.alive()) return;
        bullet.setHp(10);
        int tx = x + 17 - 3;
        int ty = y + 17 - 3;
        switch (direction) {
            case UP:
                tx = x + width / 2 - 3;
                ty = y - 10;
                break;
            case DOWN:
                tx = x + width / 2 - 3;
                ty = y + height;
                break;
            case LEFT:
                tx = x - 10;
                ty = y + height / 2 - 3;
                break;
            case RIGHT:
                tx = x + width;
                ty = y + height / 2 - 3;
                break;
        }
        bullet.setX(tx);
        bullet.setY(ty);
        bullet.setDirection(direction);
        System.out.println(ElementBean.Substance.getService().getElementList().size());
        ElementBean.Substance.getService().add(bullet);
    }

    public void stay() {
        x = oldX;
        y = oldY;
//        System.out.println(x + " " + y + " | " + oldX + " " + oldY);
    }

    @Override
    protected void move() {
        oldX = x;
        oldY = y;
        if (Keys.UP.use()) {
            if (y > 0) {
                y = y - speed;
            }
            this.direction = Direction.UP;
        } else if (Keys.DOWN.use()) {
            if (y + height + speed <= Constant.FRAME_HEIGHT - height) {
                y = y + speed;
            } else {
                y = Constant.FRAME_HEIGHT - height * 2 - speed;
            }
            this.direction = Direction.DOWN;
        } else if (Keys.LEFT.use()) {
            if (this.x > 0) {
                x = x - speed;
            }
            this.direction = Direction.LEFT;
        } else if (Keys.RIGHT.use()) {
            if (this.x + width + speed <= Constant.FRAME_WIDTH) {
                x = x + speed;
            } else {
                x = Constant.FRAME_WIDTH - width - speed;
            }
            this.direction = Direction.RIGHT;
        }
//        System.out.println(x + " " + y + " | " + oldX + " " + oldY);
    }

    @Override
    public void drawImage(Graphics g) {
        spriteMap.get(direction).render(g, x, y);
    }
}
