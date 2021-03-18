package sample.content.player;

import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.auxiliary.Keys;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IElement;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@IElement()
public class Player extends BaseElement {
    int oldX, oldY;
    private final static int SPRITE_SCALE = Constant.ELEMENT_SIZE;
    private float scale;
    private Heading					heading;
    private Map<Heading, Sprite> spriteMap;

    private enum Heading {
        UP(0 * SPRITE_SCALE, 0 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE),
        RIGHT(2 * SPRITE_SCALE, 0 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE),
        DOWN(4 * SPRITE_SCALE, 0 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE),
        LEFT(6 * SPRITE_SCALE, 0 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE);
        private int	x, y, h, w;

        Heading(int x, int y, int h, int w) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
        protected BufferedImage texture(TextureAtlas atlas) {
            return atlas.cut(x, y, w, h);
        }
    }

    public Player(TextureAtlas atlas) {
        super(100, 100);
        heading = Heading.UP;
        scale = 1;
        spriteMap = new HashMap<>();
        for(Heading h : Heading.values()) {
            SpriteSheet sheet = new SpriteSheet(h.texture(atlas), 1, SPRITE_SCALE);
            Sprite sprite = new Sprite(sheet, scale);
            spriteMap.put(h,sprite);
        }
    }

    @Override
    public void action() {
        super.action();
    }

    @Override
    protected void move() {
        if (Keys.UP.use()) {
            if (y > 0) {
                y = y - speed;
            }
            this.direction = Direction.UP;
            heading = Heading.UP;
        } else if (Keys.DOWN.use()) {
            if (y + height <= Constant.FRAME_HEIGHT) {
                y = y + speed;
            } else {
                y = Constant.FRAME_HEIGHT - height;
            }
            this.direction = Direction.DOWN;
            heading = Heading.DOWN;
        } else if (Keys.LEFT.use()) {
            if (this.x > 0) {
                x = x - speed;
            }
            this.direction = Direction.LEFT;
            heading = Heading.LEFT;
        } else if (Keys.RIGHT.use()) {
            if (this.x + width <= Constant.FRAME_WIDTH) {
                x = x + speed;
            } else {
                x = Constant.FRAME_WIDTH - width;
            }
            this.direction = Direction.RIGHT;
            heading = Heading.RIGHT;
        }

    }

    @Override
    public void drawImage(Graphics g) {

        spriteMap.get(heading).render(g, x, y);
    }
}
