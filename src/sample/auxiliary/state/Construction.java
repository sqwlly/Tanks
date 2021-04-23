package sample.auxiliary.state;

import sample.auxiliary.Constant;
import sample.auxiliary.GameStateManager;
import sample.auxiliary.Keys;
import sample.auxiliary.Map;
import sample.auxiliary.graphics.Sprite;
import sample.auxiliary.graphics.SpriteSheet;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.IDraw;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Construction extends GameState{
    private static class Choice implements IDraw {
        private static final int W = Constant.ELEMENT_SIZE / 2, H = Constant.ELEMENT_SIZE / 2;
        public int STEP;
        public int x, y;
        public int value;
        public Choice(int STEP, int side_Lx, int side_Ly, int side_Rx, int side_Ry, int type) {
            this.STEP = STEP;
            this.value = 0;
            this.x = side_Lx;
            this.y = side_Ly;
            this.side_Lx = side_Lx;
            this.side_Rx = side_Rx;
            this.side_Ly = side_Ly;
            this.side_Ry = side_Ry;
            SpriteSheet sheet = new SpriteSheet(TextureAtlas.cut(10 * Constant.ELEMENT_SIZE, 7 * Constant.ELEMENT_SIZE,
                    Constant.ELEMENT_SIZE * 3, Constant.ELEMENT_SIZE), Constant.ELEMENT_SIZE, Constant.ELEMENT_SIZE);
            rect = new Sprite(sheet, 1, type % 3);
        }

        public int side_Lx;
        public int side_Rx;
        public int side_Ly;
        public int side_Ry;
        public Sprite rect;

        public void up() {
            if(y - STEP < side_Ly) {
                y = side_Ly;
            }else{
                y -= STEP;
            }
        }

        public void down() {
            if(y + STEP > side_Ry - STEP) {
                y = side_Ry - STEP;
            }else{
                y += STEP;
            }
        }

        public void left() {
            if(x - STEP < side_Lx) {
                x = side_Lx;
            }else{
                x -= STEP;
            }
        }

        public void right() {
            if(x + STEP > side_Rx - STEP) {
                x = side_Rx - STEP;
            }else{
                x += STEP;
            }
        }

        @Override
        public void drawImage(Graphics g) {
            rect.render(g, x, y, W + 4, H + 4);
        }
    }

    public static final int[] entity = new int[] {1,2,3,5};

    private GameStateManager gsm;
    private Map map;
    private final int[][] cells = new int[Map.WIDTH][Map.HEIGHT];
    private final List<IDraw> sprites = new ArrayList<>();
    private Choice choice, cell;
    private Rectangle save;

    public Construction(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    @Override
    public void init() {
        int w = Constant.ELEMENT_SIZE / 2;
        map = new Map("/levels/Level_0", gsm);
        int tx = w * 27 + 7, ty = w * 2;

        //实际上这里Entity用一个枚举类更好一些，扩展性更好
        sprites.add(map.getEntity(1, tx, ty));
        sprites.add(map.getEntity(2, tx, ty + w * 2));
        sprites.add(map.getEntity(3, tx, ty + w * 4));
        sprites.add(map.getEntity(5, tx, ty + w * 6));
        choice = new Choice(w * 2,tx, ty, tx + w, ty + w * 8, 0);

        cell = new Choice(w, 0,0, Constant.GAME_WIDTH, Constant.GAME_HEIGHT, 1);
        int gap = Constant.FRAME_WIDTH - Constant.GAME_WIDTH;
        save = new Rectangle(Constant.GAME_WIDTH + (gap - (w * 2 + w / 3)) / 2, ty + w * 20, w * 2 + w / 3, w * 3 / 2);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(new Rectangle(x, y).intersects(save)) {
            save();
            System.out.println("save");
            progress.set("levelToPlay", "0");
            try {
                Thread.sleep(200);
            }catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            gsm.setGameState(STATE.MENU);
        }
    }

    public void save() {
        for(int i = 0; i < Map.HEIGHT; ++i) {
            for(int j = 0; j < Map.WIDTH; ++j) {
                map.setCell(j, i, cells[i][j]);
            }
        }
        map.writeFile();
    }

    @Override
    public void stateAction() {
        if(Keys.PLAY2_UP.use()) {
            choice.up();
            if(cell.value - 1 >= 0) {
                cell.value--;
            }
        }else if(Keys.PLAY2_DOWN.use()) {
            choice.down();
            if(cell.value + 1 < entity.length) {
                cell.value++;
            }
        }
        if(Keys.UP.use()) {
            cell.up();
        }else if(Keys.DOWN.use()) {
            cell.down();
        }else if(Keys.LEFT.use()) {
            cell.left();
        }else if(Keys.RIGHT.use()) {
            cell.right();
        }
        if(Keys.ENTER.use()) {
            cells[cell.x / cell.STEP][cell.y / cell.STEP] = entity[cell.value];
        }
        if(Keys.BACKSPACE.use()) {
            cells[cell.x / cell.STEP][cell.y / cell.STEP] = 0;
        }
    }

    @Override
    public void drawImage(Graphics g) {

        //地图格子绘画
        g.setColor(Color.decode("#636363"));
        for(int i = 0; i < Map.HEIGHT; ++i) {
            for(int j = 0; j < Map.WIDTH; ++j) {
                int tx = i * Constant.ELEMENT_SIZE / 2, ty = j * Constant.ELEMENT_SIZE / 2;
                g.drawRect(tx, ty, Constant.ELEMENT_SIZE / 2 + 1, Constant.ELEMENT_SIZE / 2);
                IDraw draw = map.getEntity(cells[i][j], tx, ty);
                if(draw != null) {
                    draw.drawImage(g);
                }
            }
        }
        //save button绘画
        g.setColor(Color.decode("#434343"));
        g.fillRect(save.x, save.y, save.width, save.height);
        g.setFont(font.deriveFont(8f));
        g.setColor(Color.WHITE);
        g.drawString("SAVE", save.x + 5, save.y + save.height / 2 + 3);

        //entity障碍物绘画
        for(IDraw draw : sprites) {
            draw.drawImage(g);
        }
        //老鹰绘画
        gsm.getHome().drawImage(g);

        //entity选择框
        choice.drawImage(g);

        //cell选择框
        cell.drawImage(g);
    }
}
