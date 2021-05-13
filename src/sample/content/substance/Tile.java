package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IElement;
import sample.content.common.Attribute;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@IElement(width = Constant.ELEMENT_SIZE / 2, height = Constant.ELEMENT_SIZE / 2, defense = 0, hp = 200)
public class Tile extends BaseElement {

    private final int[] state;
    private int[][] sie;
    private final List<Sprite> sprites = new ArrayList<>();
    private final ArrayList<Rectangle> rec = new ArrayList<>();

    public Tile(int x, int y) {
        super(x, y);
        for(int i = 0; i < 2; ++i) {
            for(int j = 0; j < 2; ++j) {
                int w = width / 2, h = height / 2;
                rec.add(new Rectangle(x + w * j, y + h * i, w, h));
            }
        }
        SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(4 * Constant.ELEMENT_SIZE, 5 * Constant.ELEMENT_SIZE,
                15 * Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
        for(int i = 0; i < sheet.getSpriteCount(); ++i) {
            sprites.add(new Sprite(sheet, 1, i));
        }
        state = new int[] {1,1,1,1};
        diversity();
    }

    /**
     * @Description 重写一下action，不可以让砖块移动哦
     * @Param []
     * @return void
     */
    @Override
    public void action() {

    }

    //1111
    //
    // 01 -> 0
    // 11
    /**
     * @Description 砖块的不同状态
     * @Param []
     * @return void
     */
    public void diversity() {
        //根据素材里墙块的不同排列，0代表缺失，1代表存在
        sie = new int[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {1, 1, 0, 0},
                {0, 0, 1, 0},
                {1, 0, 1, 0},
                {0, 1, 1, 0},
                {1, 1, 1, 0},
                {0, 0, 0, 1},
                {1, 0, 0, 1},
                {0, 1, 0, 1},
                {1, 1, 0, 1},
                {0, 0, 1, 1},
                {1, 0, 1, 1},
                {0, 1, 1, 1},
                {1, 1, 1, 1},
                {0, 0, 0, 0}
        };
    }

    /**
     * @Description 将四个角中的其中一个角状态设为0,  1 -> 0
     * @Param [idx]
     * @return void
     */
    public void hitState(int idx) {
        state[idx % 4] = 0;
    }

    public int getRecState(int idx) {
        return state[idx];
    }

    /**
     * @Description 当前砖块状态
     * @Param []
     * @return int
     */
    public int getState() {
        for(int i = 0; i < sie.length; ++i) {
            boolean same = true;
            for(int j = 0; j < state.length; ++j) {
//                System.out.println(state[j] + " " + sie[i][j]);
                if(sie[i][j] != state[j]) {
                    same = false;
                    break;
                }
            }
//            System.out.println("------------------");
            if(same) {
//                System.out.println(i);
                return i;
            }
        }
        return sie.length - 1;
    }

    public ArrayList<Rectangle> getRec() {
        return rec;
    }

    /**
     * @Description 重构一下绘画方法，根据type不同画出不同大小砖块
     * @Param [g]
     * @return void
     */
    @Override
    public void drawImage(Graphics g) {
        if(!alive()) return; //temp handle
        if(getState() == sie.length - 1) {
            die();
            return;
        }
        sprites.get(getState()).render(g, x, y, Constant.ELEMENT_SIZE + 1, Constant.ELEMENT_SIZE + 1);
    }
}
