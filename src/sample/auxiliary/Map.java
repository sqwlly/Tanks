package sample.auxiliary;

import sample.base.IDraw;
import sample.content.enemy.Enemy;
import sample.content.substance.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class Map {
    public static final int WIDTH = 26, HEIGHT = 26;
    private int[][] map;
    private IDraw[][] cells; //先暂时放着吧
    private Queue<Enemy> enemies;
    private String[] enemyType;
    private final String fileName;
    private GameStateManager gsm;
    public int getSumReward() {
        return sumReward;
    }

    private int sumReward;

    //player是独立于map之外的，因为进入下一关player的状态不变，是同一个player
    public Map(String file, GameStateManager gsm) {
        this.gsm = gsm;
        this.fileName = file;
        init();
    }

    public void init() {
        BufferedReader br = ResourceLoader.loadMapConfig(fileName);
        String delimiters = "";
        try {
            map = new int[WIDTH][HEIGHT];
            String line = br.readLine();
            if(line != null) {
                enemyType = line.split(delimiters);
            }
            for(int i = 0; i < HEIGHT; ++i) {
                String s = br.readLine();;
                if(s == null) break;
                String[] msg = s.split(delimiters);
//                System.out.println(s + "  " + msg.length);
                for(int j = 0; j < WIDTH && j < msg.length; ++j) {
                    map[i][j] = Integer.parseInt(msg[j]);
                }
            }
            loadMap();
        }catch (IOException ignored) {

        }
    }

    public void loadMap() {
        enemyInit();
        ElementBean.Substance.getService().add(gsm.getHome());
        for(int i = 0; i < HEIGHT; ++i) {
            for(int j = 0; j < WIDTH; ++j) {
                IDraw draw = getEntity(map[i][j], j * Constant.ELEMENT_SIZE / 2, i * Constant.ELEMENT_SIZE / 2);
                if(draw != null) {
                    ElementBean.Substance.getService().add(draw);
                }
                //getEntity(map[i][j], j * Constant.ELEMENT_SIZE / 2, i * Constant.ELEMENT_SIZE / 2);
            }
        }
    }

    public IDraw getEntity(int type, int x, int y) {
        IDraw draw = null;
        switch (type) {
            //钢铁
            case 1:
                draw = new Steel(x, y);
                //ElementBean.Substance.getService().add(new Steel(x, y));
                break;
            //砖瓦
            case 2:
                draw = new Tile(x, y);
//                ElementBean.Substance.getService().add(new Tile(x, y, 1));
                break;
            //草丛
            case 3:
                draw = new Grass(x, y);
//                ElementBean.Substance.getService().add(new Grass(x, y));
                break;
            //河流
            case 5:
                draw = new Water(x, y);
//                ElementBean.Substance.getService().add(new Water(x, y));
                break;
        }
        return draw;
    }

    public void enemyInit() {
        enemies = new LinkedList<>();
        if(enemyType == null) return;
        for (String s : enemyType) {
            sumReward += Enemy.REWARD[Integer.parseInt(s) - 1];
            int x;
            //随机产生横坐标，并且保证当前坐标没有障碍物（即为空0）
            do {
                x = CommonUtils.nextInt(0, 24);
//                System.out.println(x + "," + 0 + " = " + getCell(0, x));
            } while (getCell(x, 0) != 0 || getCell(x, 1) != 0);
            enemies.add(new Enemy(x / 2 * Constant.ELEMENT_SIZE, 0, Integer.parseInt(s) - 1));
        }

    }

    public void writeFile() {
        BufferedWriter bufferedWriter = ResourceLoader.writeFile("/levels/Level_0");
        try {
            bufferedWriter.write("11223344111231423143");
            bufferedWriter.newLine();
            for(int i = 0; i < HEIGHT; ++i) {
                for(int j = 0; j < WIDTH; ++j) {
                    bufferedWriter.write(map[i][j] + "");
                }
                bufferedWriter.newLine();
            }

            bufferedWriter.flush();
            bufferedWriter.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void enemyBorn() {
        for (int i = 0; i < 1; ++i) {
            if (!enemies.isEmpty()) {
                Enemy enemy = enemies.poll();
                ElementBean.Enemy.getService().add(enemy);
                ElementBean.Substance.getService().add(enemy.getBorn());
            }
        }
    }

    public int getCell(int x, int y) {
        return map[y][x];
    }

    public void setCell(int x, int y, int value) {
        map[y][x] = value;
    }

    public int[] getRow(int i) {
        return map[i];
    }

    public int[] getCol(int i) {
        int[] t = new int[HEIGHT];
        for(int j = 0; j < HEIGHT; ++j) {
            t[j] = map[j][i];
        }
        return t;
    }

}
