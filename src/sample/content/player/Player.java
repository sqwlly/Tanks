package sample.content.player;

import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.auxiliary.ElementBean;
import sample.auxiliary.Keys;
import sample.auxiliary.graphics.Animation;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.*;
import sample.content.common.Tank;
import sample.content.substance.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@IElement(width = Constant.ELEMENT_SIZE - 2, height = Constant.ELEMENT_SIZE - 2, speed = 2)
public class Player extends Tank {
    private final static int size = Constant.ELEMENT_SIZE;

    private final static float scale = 1f;
    private int score;
    private int level;
    private final List<HashMap<Direction, Animation>> sprite = new ArrayList<>();
    private Invincible invincible;
    private Born born;

    public void addLevel() {
        if(level + 1 < 4) {
            level++;
        };
    }

    public void born() {
        this.hp.setValue(50);
    }

    public void initLevel() {
        level = 0;
    }

    public void setBorn(Born born) {
        this.born = born;
    }

    public Player(int x, int y) {
        super(x, y);
        int c = 0;
        BufferedImage[] act = new BufferedImage[2];
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(0, 0, size * 32, size), size, size);
        for(int i = 0; i < 4; ++i) {
            //一共四个方向，四种形态
            HashMap<Direction, Animation> sprites = new HashMap<>();
            for(Direction d : Direction.values()) {
                //一个方向有两帧
                for(int j = 0; j < 2; ++j) {
                    act[j] = sheet.getSprite(c++);
                }
                Animation animation = new Animation(act, 50);
                sprites.put(d, animation);
                animation.start();
            }
            //将一种形态的四个方向animation加入sprite列表，随后可以通过level来分别取得不同形态
            sprite.add(sprites);
        }
        initLevel();
        born = new Born(x, y);
        bulletNumInit();
    }

    public void beHurt() {

    }

    public void initScore() {
        score = 0;
    }

    public void addScore(int base) {
        score += base;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void action() {
        move();
        invincible.movedByPlayer(this);
        if (Keys.SPACE.use()) {
            shoot();
        }
    }

    //无敌状态
    public void beInvincible() {
        ElementBean.Substance.getService().remove(invincible);
        invincible = new Invincible(x, y);
        ElementBean.Substance.getService().add(invincible);
    }

    public Invincible getInvincible() {
        return invincible;
    }

    public Born getBorn() {
        return born;
    }

    @Override
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
        Bullet bullet = new Bullet(tx, ty, direction, this);
        bullet.setLevel(level);
        ElementBean.Player.getService().add(bullet);
    }

    @Override
    public void move() {
        oldX = x;
        oldY = y;
        if (Keys.UP.use()) {
            if (y - speed.getValue() > 0) {
                y = y - speed.getValue();
            }
            this.direction = Direction.UP;
        } else if (Keys.DOWN.use()) {
            if (y + speed.getValue() < Constant.FRAME_HEIGHT - height * 2 + 3) {
                y = y + speed.getValue();
            } else {
                y = Constant.FRAME_HEIGHT - height * 2 + 3;
            }
            this.direction = Direction.DOWN;
        } else if (Keys.LEFT.use()) {
            if (this.x - speed.getValue() > 0) {
                x = x - speed.getValue();
            }else{
                x = 0;
            }
            this.direction = Direction.LEFT;
        } else if (Keys.RIGHT.use()) {
            if (this.x + width + speed.getValue() < Constant.GAME_WIDTH) {
                x = x + speed.getValue();
            } else {
                x = Constant.GAME_WIDTH - width;
            }
            this.direction = Direction.RIGHT;
        }
    }

    @Override
    public void drawImage(Graphics g) {
        g.drawImage(sprite.get(level).get(direction).getSprite(), x, y, width, height, null);
        sprite.get(level).get(direction).update();
    }
}
