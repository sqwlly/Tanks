package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.IElement;
import sample.content.player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@IElement(width = Constant.ELEMENT_SIZE / 2, height = Constant.ELEMENT_SIZE / 2)
public class Tile extends BaseElement {
    
    private int type;
    private int[] state = new int[4];
    private int[][] sie;
    private SpriteSheet sheet;
    private List<Sprite> sprites = new ArrayList<>();


    public Tile(int x, int y, int type) {
        super(x, y);
        this.type = type;
        sheet = new SpriteSheet(TextureAtlas.cut(4 * Constant.ELEMENT_SIZE, 5 * Constant.ELEMENT_SIZE,
                15 * Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE);
        for(int i = 0; i < sheet.getSpriteCount(); ++i) {
            sprites.add(new Sprite(sheet, 1, i));
        }
        //for(int i = 0; i < 4; ++i) state[i] = 1;
        state = new int[] {1,0,1,1};
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

    @Override
    public boolean encounterPlayer(Player player) {
        if(this.getRectangle().intersects(player.getRectangle())) {
//            System.out.println("player stay");
            player.stay();
            return true;
        }
        return super.encounterPlayer(player);
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
                {1, 0, 1, 0},
                {0, 1, 0, 1},
                {1, 1, 0, 1},
                {0, 0, 1, 1},
                {1, 0, 1, 1},
                {0, 1, 1, 1},
                {1, 1, 1, 1}
        };
    }

    /**
     * @Description 四个角存在状态翻转, 0 -> 1 or 1 -> 0
     * @Param [idx]
     * @return void
     */
    public void flipState(int idx) {
        if(state[idx] == 0) {
            state[idx] = 1;
        }else{
            state[idx] = 0;
        }

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
                System.out.println(state[j] + " " + sie[i][j]);
                if(sie[i][j] != state[j]) {
                    same = false;
                    //break;
                }
            }
            System.out.println("------------------");
            if(same) {
                System.out.println(i);
                return i;
            }
        }
        return sie.length - 1;
    }

    /**
     * @Description 重构一下绘画方法，根据type不同画出不同大小砖块
     * @Param [g]
     * @return void
     */
    @Override
    public void drawImage(Graphics g) {
        switch (type) {
            case 0:
                for(int i = 0; i < 2; ++i) {
                    for(int j = 0; j < 2; ++j) {
                        sprites.get(5).render(g, x + j * width, y + height * i, width * 2 + 1, height * 2 + 1);
                    }
                }
                break;
            case 1:
                sprites.get(getState()).render(g, x, y, width * 2 + 1, height * 2 + 1);
                break;
        }
    }
}
