package sample.content.player;

import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.auxiliary.ElementBean;
import sample.auxiliary.Keys;
import sample.auxiliary.graphics.Animation;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.*;
import sample.content.substance.Born;
import sample.content.substance.Bullet;
import sample.content.substance.GameOver;
import sample.content.substance.Invincible;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@IElement(width = Constant.ELEMENT_SIZE - 3, height = Constant.ELEMENT_SIZE - 3, speed = 2)
public class Player extends Tank{
    private final static int size = Constant.ELEMENT_SIZE;

    private final static float scale = 1f;
    private int score;
    private final HashMap<Direction, Animation> sprites = new HashMap<>();
    private Invincible invincible;
    private final Born born;
    private Animation animation;
    public Player(int x, int y) {
        super(x, y);
        int cnt = 0;
        BufferedImage[] act = new BufferedImage[2];
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(0, 0, size * 8, size), size, size);
        for(Direction d : Direction.values()) {
            for(int i = 0; i < 2; ++i) {
                act[i] = sheet.getSprite(cnt++);
            }
            animation = new Animation(act, 50);
            sprites.put(d, animation);
            animation.start();
        }
        born = new Born(x, y);
        invincible = new Invincible(x, y);
        bulletNum = 1;
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
       // System.out.println(getHp());
       // if(alive()) {
            move();
            invincible.movedByPlayer(this);
            if (Keys.SPACE.use()) {
                shoot();
            }
//        }else {
//            if (gameOver == null) {
//                gameOver = new GameOver();
//                System.out.println("new GameOver");
//                ElementBean.Substance.getService().add(gameOver);
//            }
//        }
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
        ElementBean.Player.getService().add(new Bullet(tx, ty, direction, this));
    }

    @Override
    public void move() {
        oldX = x;
        oldY = y;
        if (Keys.UP.use()) {
            if (y - speed > 0) {
                y = y - speed;
            }
            this.direction = Direction.UP;
        } else if (Keys.DOWN.use()) {
            if (y + speed < Constant.FRAME_HEIGHT - height * 2 + 3) {
                y = y + speed;
            } else {
                y = Constant.FRAME_HEIGHT - height * 2 + 3;
            }
            this.direction = Direction.DOWN;
        } else if (Keys.LEFT.use()) {
            if (this.x - speed > 0) {
                x = x - speed;
            }else{
                x = 0;
            }
            this.direction = Direction.LEFT;
        } else if (Keys.RIGHT.use()) {
            if (this.x + width + speed < Constant.GAME_WIDTH) {
                x = x + speed;
            } else {
                x = Constant.GAME_WIDTH - width;
            }
            this.direction = Direction.RIGHT;
        }
    }

    @Override
    public void drawImage(Graphics g) {
        g.drawImage(sprites.get(direction).getSprite(), x, y, width, height, null);
        sprites.get(direction).update();
    }
}
