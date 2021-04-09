package sample.content.enemy;

import sample.auxiliary.CommonUtils;
import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.auxiliary.ElementBean;
import sample.auxiliary.graphics.Animation;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.*;
import sample.content.substance.Born;
import sample.content.substance.Bullet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@IElement(width = Constant.ELEMENT_SIZE - 2, height = Constant.ELEMENT_SIZE - 2, speed = 2)
public class Enemy extends Tank {
    public int getType() {
        return type;
    }

    private int type;
    private Born born;
    private final List<HashMap<Direction, Animation>> sprites;
    public final static int[] REWARD = {100,200,300,400};
    private int step;
    private Animation animation;

    public Enemy(int x, int y, int type) {
        super(x, y);
        this.type = type;
        this.direction = Direction.DOWN;
        sprites = new ArrayList<>();
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(0, 2 * Constant.ELEMENT_SIZE,
                Constant.ELEMENT_SIZE * 32, Constant.ELEMENT_SIZE),
                Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        int c = 0;
        for(int i = 0; i < 4; ++i) {
            HashMap<Direction, Animation> spriteMap = new HashMap<>();
            BufferedImage[] act = new BufferedImage[2];
            for(Direction d : Direction.values()) {
                for(int j = 0; j < 2; ++j) {
                    act[j] = sheet.getSprite(c++);
                }
                animation = new Animation(act, 50);
                animation.start();
                spriteMap.put(d, animation);
            }
            sprites.add(spriteMap);
        }
        bulletNumInit();
        born = new Born(x, y);
        if(type == 3) {
            hp.setValue(hp.getValue() * 4);
        }
    }

    public int getReward() {
        return REWARD[type];
    }

    public Born getBorn() {
        return born;
    }

    public void shoot() {
        if(!fireAble()) return;
        bulletNum--;
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
        ElementBean.Enemy.getService().add(new Bullet(tx, ty, direction, this));
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
    public void action() {
        move();
    }

    @Override
    public void move() {
        oldX = x;
        oldY = y;

        if (direction.up()) {
            if (y - speed.getValue() >= 0) {
                this.y -= speed.getValue();
            }else{
                y = 0;
            }
        } else if (direction.down()) {
            if (y + speed.getValue() + height * 2 <= Constant.FRAME_HEIGHT + 3) {
                this.y += speed.getValue();
            } else {
                y = Constant.FRAME_HEIGHT - height * 2 + 3;
            }
        } else if (direction.right()) {
            if (x + speed.getValue() + width <= Constant.GAME_WIDTH) {
                this.x += speed.getValue();
            }else{
                x = Constant.GAME_WIDTH - width;
            }
        } else if (direction.left()) {
            if (x - speed.getValue() >= 0) {
                this.x -= speed.getValue();
            }else{
                this.x = 0;
            }
        }

        step--;
        if (step <= 0) {
            int r = CommonUtils.nextInt(0, 4);
            direction = Direction.values()[r];
            step = CommonUtils.nextInt(0, 50) + 30;
            shoot();
        }
    }

    @Override
    public void drawImage(Graphics g) {
        sprites.get(type).get(direction).update();
        g.drawImage(sprites.get(type).get(direction).getSprite(), x, y, width, height, null);
    }
}
