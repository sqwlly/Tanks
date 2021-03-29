package sample.content.enemy;

import sample.auxiliary.CommonUtils;
import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.auxiliary.ElementBean;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IElement;
import sample.base.IMovable;
import sample.content.player.Player;
import sample.content.substance.Born;
import sample.content.substance.Bullet;
import sample.content.substance.EnemyBullet;
import sample.content.substance.Home;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@IElement(width = Constant.ELEMENT_SIZE - 2, height = Constant.ELEMENT_SIZE - 2)
public class Enemy extends BaseElement implements IMovable {
    private int type;
    private Born born;
    private Bullet bullet;
    private final List<HashMap<Direction, Sprite>> sprites;
    private int oldX, oldY;
    private int step;
    private int bulletNum;

    public Enemy(int x, int y, int type) {
        super(x, y);
        this.type = type;
        this.direction = Direction.DOWN;
        sprites = new ArrayList<>();
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(0, 2 * Constant.ELEMENT_SIZE,
                Constant.ELEMENT_SIZE * 32, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE);
        int c = 0;
        for(int i = 0; i < 4; ++i) {
            HashMap<Direction, Sprite> spriteMap = new HashMap<>();
            for(Direction d : Direction.values()) {
                spriteMap.put(d, new Sprite(sheet, 1, c));
                c += 2;
            }
            sprites.add(spriteMap);
        }
        speed = 2;
        bulletNum = 1;
//        bullet = new EnemyBullet(-10, y, direction);
        born = new Born(x, y);
//        bullet.die();
    }

    private void shoot() {
//        System.out.println(bullet.getHp());
//        System.out.println(bullet.toString() + " " +  bullet.getX() + " " + bullet.getY());
//        if(bulletNum <= 0) return;
//        bulletNum--;
       // bullet.setHp(10);
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
//        bullet.setX(tx);
//        bullet.setY(ty);
//        bullet.setDirection(direction);
        bullet = new Bullet(tx, ty, direction);
        ElementBean.Enemy.getService().add(bullet);
//        System.out.println(this.toString() + " shoot" + "|" + ElementBean.Enemy.getService().getElementList().size());
    }

    @Override
    public void stay() {
        x = oldX;
        y = oldY;
//        System.out.println("stay " + x + " " + y);
    }

    @Override
    public void die() {
        super.die();
    }

    @Override
    public boolean alive() {
        return super.alive();
    }

    @Override
    public boolean encounterSide() {
        if(x - speed < 0 || x + speed > Constant.FRAME_WIDTH || y - speed < 0 || y + speed > Constant.FRAME_HEIGHT) {
            //stay();
            return true;
        }
        return super.encounterSide();
    }

    @Override
    public void action() {
        move();
//        if(encounterSide()) {
////            System.out.println("stay");
//            stay();
//        }
    }

    @Override
    protected void move() {

        shoot();
        oldX = x;
        oldY = y;

        if(direction.up()) {
            if(y - speed >= 0)
            this.y -= speed;
        }else if(direction.down()) {
            if(y + speed + height <= Constant.FRAME_HEIGHT)
            this.y += speed;
        }else if(direction.right()) {
            if(x + speed + width <= Constant.FRAME_WIDTH)
            this.x += speed;
        }else if(direction.left()) {
            if(x - speed >= 0)
            this.x -= speed;
        }


        step--;
        if(step <= 0) {
            int r = CommonUtils.nextInt(0, 4);
            direction = Direction.values()[r];
            step = CommonUtils.nextInt(0, 50) + 30;
        }
//        System.out.println(this.toString() + "|step = " + step + "| "+ "direction "+ direction+" |" +x + " " + y);
    }

    @Override
    public void drawImage(Graphics g) {
        sprites.get(type).get(direction).render(g, x, y, width, height);
    }
}
