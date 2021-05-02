package sample.content.enemy;

import sample.auxiliary.CommonUtils;
import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.auxiliary.ElementBean;
import sample.auxiliary.graphics.Animation;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.*;
import sample.content.common.Tank;
import sample.content.substance.Born;
import sample.content.substance.Bullet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@IElement(width = Constant.ELEMENT_SIZE - 2, height = Constant.ELEMENT_SIZE - 2, speed = 2)
public class Enemy extends Tank {
    private int type;
    private final List<HashMap<Direction, Animation>> sprites;
    public final static int[] REWARD = {100,200,300,400};

    public int getStep() {
        return step;
    }

    public void stepReduce() {
        step--;
    }

    public void setStep(int step) {
        this.step = step;
    }

    private int step;
    private int level;
    private int fire;

    public EnemyState getEnemyState() {
        return enemyState;
    }

    private EnemyState enemyState;

    public void setEnemyMode(EnemyMode enemyMode) {
        this.enemyMode = enemyMode;
    }

    private EnemyMode enemyMode;

    public EnemyMode getEnemyMode() {
        return enemyMode;
    }

    public boolean fire() {
        return fire >= 1 && fire <= 4;
    }

    public void randomFire() {
        this.fire = CommonUtils.nextInt(0, step);
    }

    public Enemy(int x, int y, int type) {
        super(x, y);
        fire = CommonUtils.nextInt(0, step);

        step = 0;
        enemyState = new EnemyState(this);
        level = 0;
        setType(type);
        this.direction = Direction.DOWN;
        sprites = new ArrayList<>();
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(0, 2 * Constant.ELEMENT_SIZE,
                Constant.ELEMENT_SIZE * 32, Constant.ELEMENT_SIZE),
                Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        int c = 0;
        for(int i = 0; i < 3; ++i) {
            HashMap<Direction, Animation> spriteMap = new HashMap<>();
            BufferedImage[] act = new BufferedImage[2];
            for(Direction d : Direction.values()) {
                for(int j = 0; j < 2; ++j) {
                    act[j] = sheet.getSprite(c++);
                }
                Animation animation = new Animation(act, 50);
                animation.start();
                spriteMap.put(d, animation);
            }
            sprites.add(spriteMap);
        }
        sheet = new SpriteSheet(TextureAtlas.cut(24 * Constant.ELEMENT_SIZE, 0,
                8 * Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE * 4), Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        HashMap<Direction, Animation> spriteMap = new HashMap<>();
        int k = 0;
        for(Direction d : Direction.values()) {
            BufferedImage[] act = new BufferedImage[4];
            c = 0;
            for(int i = 0; i < 2; ++i) {
                //0 4 -> 1 5
                //i i+4 -> i+1 i+4
                act[c++] = sheet.getSprite(k + i + 8);
                act[c++] = sheet.getSprite(k + i + 16);
            }
            k += 2;
            Animation animation = new Animation(act, 10);
            animation.start();
            spriteMap.put(d, animation);
        }
        sprites.add(spriteMap);
        bulletNumInit();
        born = new Born(x, y);
        enemyMode = EnemyMode.SIMPLE;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        if(type == 3 || type == 4) {
            hp.setValue(hp.getValue() * 2);
            speed.setValue(1);
        }else if(type == 2) {
            level = 1;
        }else if(type == 1) {
            this.speed.setValue(this.getSpeed() + 1);
        }
    }

    @Override
    public void stay() {
        super.stay();
        int nxd = CommonUtils.nextInt(0, 12);
        if(nxd == 11) {
            int r = CommonUtils.nextInt(0, 4);
            direction = Direction.values()[r];
        }
        if(enemyMode == EnemyMode.SIMPLE) {
            step = CommonUtils.nextInt(0, 10) + 10;
        }
    }

    public void addLevel() {
        if(level + 2 < 2) {
            level += 2;
        }
    }

    public int getReward() {
        return REWARD[type];
    }

    public Born getBorn() {
        return born;
    }

    public void shoot() {
        randomFire();
        if(!fire()) return;
//        System.out.println("fire = " + fire);
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
        ElementBean.Enemy.getService().add(bullet);
    }

    @Override
    public boolean beforeActionJudge() {
        if(!born.isComplete()) {
            return false;
        }
        return super.beforeActionJudge();
    }

    @Override
    public boolean alive() {
        return super.alive();
    }

    @Override
    public void action() {
        if(enemyMode == EnemyMode.SIMPLE) {
            enemyState.simpleRandomMove();
        }else if(enemyMode == EnemyMode.INTELLIGENT){
            enemyState.AIMove();
        }
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
//
//        step--;
//        if (step <= 0) {
//            int r = CommonUtils.nextInt(0, 4);
//            direction = Direction.values()[r];
//            step = CommonUtils.nextInt(0, 50) + 10;
//            shoot();
//        }
//        Audio.enemy_move.play();
    }

    @Override
    public void drawImage(Graphics g) {
        if(!born.isComplete()) return;
        sprites.get(type).get(direction).update();
        g.drawImage(sprites.get(type).get(direction).getSprite(), x, y, width, height, null);
    }
}
