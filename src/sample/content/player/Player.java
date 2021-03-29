package sample.content.player;

import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.auxiliary.ElementBean;
import sample.auxiliary.Keys;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.*;
import sample.content.substance.Born;
import sample.content.substance.Bullet;
import sample.content.substance.Invincible;

import java.awt.*;
import java.util.HashMap;

@IElement(width = Constant.ELEMENT_SIZE - 3, height = Constant.ELEMENT_SIZE - 3)
public class Player extends BaseElement implements IBulletCross, ITankCross, IMovable {
    private final static int size = Constant.ELEMENT_SIZE;

    private final static float scale = 1f;

    private HashMap<Direction, Sprite> spriteMap;
    private Bullet bullet;

    private Invincible invincible;
    private final Born born;
    private int oldX, oldY;

    public int getBulletNum() {
        return bulletNum;
    }

    public void setBulletNum(int bulletNum) {
        this.bulletNum = bulletNum;
    }

    private int bulletNum;

    public Player(int x, int y) {
        super(x, y);
        this.speed = 2;
        spriteMap = new HashMap<>();
        int cnt = 0;
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(0, 0, size * 8, size), size);
        for(Direction d : Direction.values()) {
            spriteMap.put(d, new Sprite(sheet, scale, cnt));
            cnt += 2;
        }
        born = new Born(x, y);
        invincible = new Invincible(x, y);
        //bullet = new Bullet(-1, -1, direction);
        //bullet.die();
        bulletNum = 1;
    }

    public void beHurt() {

    }

    @Override
    public void action() {
        move();
        invincible.movedByPlayer(this);
        if(Keys.SPACE.use()) {
            shoot();
        }
        if(bulletNum <= 0) {
            bulletNum += 1;
        }
    }

    public Invincible getInvincible() {
        return invincible;
    }

    public Bullet getBullet() {
        return bullet;
    }

    public Born getBorn() {
        return born;
    }

    public void shoot() {
//        if(bullet.alive()) return;
//        bullet.setHp(10);
        if(bulletNum <= 0) return;
        bulletNum--;
        //setBulletNum(0);
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
        bullet = new Bullet(tx, ty, direction);
//        bullet.setX(tx);
//        bullet.setY(ty);
//        bullet.setDirection(direction);
//        System.out.println(ElementBean.Substance.getService().getElementList().size());
        ElementBean.Player.getService().add(bullet);
//        System.out.println(ElementBean.Player.getService().getElementList().size());
    }

    @Override
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
            if (y - speed > 0) {
                y = y - speed;
            }
            this.direction = Direction.UP;
        } else if (Keys.DOWN.use()) {
            if (y + speed < Constant.FRAME_HEIGHT - height * 2) {
                y = y + speed;
            } else {
                y = Constant.FRAME_HEIGHT - height * 2 - speed;
            }
            this.direction = Direction.DOWN;
        } else if (Keys.LEFT.use()) {
            if (this.x - speed > 0) {
                x = x - speed;
            }
            this.direction = Direction.LEFT;
        } else if (Keys.RIGHT.use()) {
            if (this.x + width + speed < Constant.FRAME_WIDTH) {
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
        spriteMap.get(direction).render(g, x, y, width, height);
    }
}
