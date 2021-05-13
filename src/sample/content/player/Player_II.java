package sample.content.player;

import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.auxiliary.Keys;
import sample.auxiliary.graphics.Animation;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.IElement;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@IElement(width = Constant.ELEMENT_SIZE - 2, height = Constant.ELEMENT_SIZE - 2, speed = 2, defense = 0)
public class Player_II extends Player{
    private List<HashMap<Direction, Animation>> sprite = new ArrayList<>();

    public Player_II(int x, int y) {
        super(x, y);
        animationInit();
    }
    @Override
    public void action() {
        if(born.isComplete()) {
            move();
            invincible.movedByPlayer(this);
            if (Keys.ZERO.use()) {
                shoot();
            }
        }
    }

    @Override
    public void move() {
        oldX = x;
        oldY = y;
        if (Keys.PLAY2_UP.use()) {
            y = Math.max(y - speed.getValue(), 0);
            this.direction = Direction.UP;
        } else if (Keys.PLAY2_DOWN.use()) {
            y = Math.min(y + speed.getValue(), Constant.FRAME_HEIGHT - height * 2 + 3);
            this.direction = Direction.DOWN;
        } else if (Keys.PLAY2_LEFT.use()) {
            x = Math.max(this.x - speed.getValue(), 0);
            this.direction = Direction.LEFT;
        } else if (Keys.PLAY2_RIGHT.use()) {
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
        if(!born.isComplete()) return;
        g.drawImage(sprite.get(level).get(direction).getSprite(), x, y, width, height, null);
        sprite.get(level).get(direction).update();
    }

    @Override
    protected void animationInit() {
        int c = 0;
        sprite = new ArrayList<>();
        BufferedImage[] act = new BufferedImage[2];
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(0, size, size * 32, size), size, size);
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
        sheet = new SpriteSheet(TextureAtlas.cut(8 * 34, 8 * 34, 8 * 34, 34),
                34, 34);
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
}
