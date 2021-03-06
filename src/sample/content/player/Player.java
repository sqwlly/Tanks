package sample.content.player;

import sample.auxiliary.*;
import sample.auxiliary.audio.Audio;
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

@IElement(width = Constant.ELEMENT_SIZE - 2, height = Constant.ELEMENT_SIZE - 2, speed = 2, defense = 0)
public class Player extends Tank {
    public final static int size = Constant.ELEMENT_SIZE;

    //暂时先不用
    private final static float scale = 1f;

    public long getBornTime() {
        return bornTime;
    }

    public void setBornTime(long bornTime) {
        this.bornTime = bornTime;
    }

    protected long bornTime;
    protected int score;
    private final List<HashMap<Direction, Animation>> sprite = new ArrayList<>();

    protected Invincible invincible;

    public void addLevel() {
        if(level + 1 < 4) {
            level++;
        };
        if(level == 3) {
            bulletNum = 2;
            //这种防御力和生命值可以抗50攻击力的子弹三下
            this.defense.setValue(20);
            this.hp.setValue(100);
        }
    }

    public void born() {
        if(this instanceof Player_II) {
            this.setX(8 * 34);
        }else {
            this.setX(4 * 34);
        }
        this.setY(12 * 34);
        this.hp.setValue(50);
        this.setDirection(Direction.UP);
        born = new Born(x, y);
        ElementBean.Substance.getService().add(born);
        beInvincible();
        bulletNumInit();
        this.defense.setValue(0);
        bornTime = 0;
//        for(int i = 0; i < 3; ++i) {
//            addLevel();
//        }
    }

    public void initLevel() {
        level = 0;
    }

    public Player(int x, int y) {
        super(x, y);
        animationInit();
        born();
    }

    protected void animationInit() {
        int c = 0;
        BufferedImage[] act = new BufferedImage[2];
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(0, 0, size * 32, size), size, size);
        for(int i = 0; i < 3; ++i) {
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
        sheet = new SpriteSheet(TextureAtlas.cut(0, 8 * Constant.ELEMENT_SIZE,
                8 * Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        HashMap<Direction, Animation> sprites = new HashMap<>();
        c = 0;
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
        invincible = new Invincible(x, y, born);
        ElementBean.Substance.getService().add(invincible);
    }

    public boolean invulnerable() {
        System.out.println(invincible.getHp().getValue());
        return invincible.alive();
    }

    @Override
    public void shoot() {
        if(!fireAble()) return;
        super.shoot();
        int tx = x + 17 - 3;
        int ty = y + 17 - 3;
        switch (direction) {
            case UP:
                tx = x + width / 2 - 4;
                ty = y - 10;
                break;
            case DOWN:
                tx = x + width / 2 - 4;
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
        Audio.bullet_shot.play();
    }

    @Override
    public void move() {
        oldX = x;
        oldY = y;
        if (Keys.UP.use()) {
            y = Math.max(y - speed.getValue(), 0);
            this.direction = Direction.UP;
        } else if (Keys.DOWN.use()) {
            y = Math.min(y + speed.getValue(), Constant.FRAME_HEIGHT - height * 2 + 3);
            this.direction = Direction.DOWN;
        } else if (Keys.LEFT.use()) {
            x = Math.max(this.x - speed.getValue(), 0);
            this.direction = Direction.LEFT;
        } else if (Keys.RIGHT.use()) {
            if (this.x + width + speed.getValue() < Constant.GAME_WIDTH) {
                x = x + speed.getValue();
            } else {
                x = Constant.GAME_WIDTH - width;
            }
            this.direction = Direction.RIGHT;
        }
//        Audio.player_move.play();
    }

    @Override
    public boolean beforeActionJudge() {
        if(!born.isComplete()) {
            return false;
        }
        return super.beforeActionJudge();
    }

    @Override
    public void drawImage(Graphics g) {
        if(!born.isComplete()) return;
        g.drawImage(sprite.get(level).get(direction).getSprite(), x, y, width, height, null);
        sprite.get(level).get(direction).update();
    }
}
