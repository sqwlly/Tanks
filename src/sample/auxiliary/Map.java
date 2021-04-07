package sample.auxiliary;

import sample.base.IDraw;
import sample.content.enemy.Enemy;
import sample.content.player.Player;
import sample.content.substance.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Map {
    private final int width = 26, height = 26;
    private int[][] map;
    private IDraw[][] cells; //先暂时放着吧
    private Player player;
    private Queue<Enemy> enemies;
    private String[] enemyType;

    public int getSumReward() {
        return sumReward;
    }

    private int sumReward;
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Map(String file) {
        BufferedReader br = ResourceLoader.loadMapConfig(file);
        String delimiters = "";
        try {
            map = new int[width][height];
            enemyType = br.readLine().split(delimiters);
            for(int i = 0; i < height; ++i) {
                String s = br.readLine();;
                if(s == null) break;
                String[] msg = s.split(delimiters);
                for(int j = 0; j < width && j < msg.length; ++j) {
                    map[i][j] = Integer.parseInt(msg[j]);
                }
            }
            loadMap();
        }catch (IOException ignored) {

        }
    }

    public void loadMap() {
        for(ElementBean bean : ElementBean.values()) {
            bean.getService().init();
        }
        ElementBean.Substance.getService().add(new Home(6 * Constant.ELEMENT_SIZE, 12 * Constant.ELEMENT_SIZE));
        playerInit();
        enemyInit();
        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                getEntity(map[i][j], j * Constant.ELEMENT_SIZE / 2, i * Constant.ELEMENT_SIZE / 2);
            }
        }
    }

    public void getEntity(int type, int x, int y) {
        switch (type) {
            case 1:
                ElementBean.Substance.getService().add(new Steel(x, y));
                break;
            case 2:
                ElementBean.Substance.getService().add(new Tile(x, y, 1));
                break;
            case 3:
                ElementBean.Substance.getService().add(new Grass(x, y));
                break;
            case 5:
                ElementBean.Substance.getService().add(new Water(x, y));
                break;
        }
    }

    public void playerInit() {
        player = new Player(4 * Constant.ELEMENT_SIZE, 12 * Constant.ELEMENT_SIZE);
        ElementBean.Player.getService().add(player);
        ElementBean.Substance.getService().add(player.getBorn());
        ElementBean.Substance.getService().add(player.getInvincible());
    }

    public void enemyInit() {
        enemies = new LinkedList<>();
        for (String s : enemyType) {
            sumReward += Enemy.REWARD[Integer.parseInt(s) - 1];
            int x;
            //随机产生横坐标，并且保证当前坐标没有障碍物（即为空0）
            do {
                x = CommonUtils.nextInt(0, 24);
//                System.out.println(x + "," + 0 + " = " + getCell(0, x));
            } while (getCell(x, 0) != 0 && getCell(x, 1) != 0);
            enemies.add(new Enemy(x / 2 * Constant.ELEMENT_SIZE, 0, Integer.parseInt(s) - 1));
        }

    }

    public void enemyBorn() {
        for (int i = 0; i < 2; ++i) {
            if (!enemies.isEmpty()) {
                Enemy enemy = enemies.poll();
                ElementBean.Enemy.getService().add(enemy);
                ElementBean.Substance.getService().add(enemy.getBorn());
            }
        }
    }

    public int getCell(int x, int y) {
        return map[x][y];
    }

    public int[] getRow(int i) {
        return map[i];
    }

    public int[] getCol(int i) {
        int[] t = new int[height];
        for(int j = 0; j < height; ++j) {
            t[j] = map[j][i];
        }
        return t;
    }

}
