package sample.content.enemy;

import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.auxiliary.ElementBean;
import sample.auxiliary.Map;
import sample.base.BaseElement;
import sample.content.player.Player;
import sample.content.substance.Grass;
import sample.content.substance.Home;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class EnemyState {

    static class Node{
        int x, y, fx, fy, step;
        public Node(int x, int y, int fx, int fy, int step) {
            this.x = x;
            this.y = y;
            this.fx = fx;
            this.fy = fy;
            this.step = step;
        }
    }
    private static int[][] dir = new int[][]{{0,-1}, {1,0}, {0, 1}, {-1, 0}};
    private Direction[] d = new Direction[] {Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT};
    private LinkedList<Direction> directions = new LinkedList<>();
    private final static int cellW = Constant.ELEMENT_SIZE / 2;

    private Map map;
    private Home home;

    public EnemyState(Map map) {
        this.map = map;
        this.home = map.getHome();
    }

    public void dfs() {
    }

    public void bfs(Enemy enemy) {
        System.out.println("start bfs");
        int width = 26, height = 26;
        boolean[][] vis = new boolean[width][height];
        int[][] dis = new int[26][26];
        for(int i = 0; i < width; ++i) {
            for(int j = 0; j < height; ++j) {
                dis[i][j] = Integer.MAX_VALUE - 1;
            }
        }
        Queue<Node> nodes = new LinkedList<>();
        int sx = enemy.getX() / cellW, sy = enemy.getY() / cellW;
        System.out.println("sx = " + sx + " | sy = " + sy);
        int dx = home.getX() / cellW, dy = home.getY() / cellW;
        nodes.add(new Node(dx, dy, -1, -1, 0));
        vis[dx][dy] = true;
        dis[dx][dy] = 0;
        while(!nodes.isEmpty()) {
            Node cur = nodes.poll();
//            if(cur.x == home.getX() / cellW && cur.y == home.getY() / cellW) {
//                System.out.println("find home" + " : " + cur.step);
//                break;
//                //return;
//            }
            for(int i = 0; i < 4; ++i) {
                int tx = cur.x + dir[i][0];
                int ty = cur.y + dir[i][1];
                if(tx < 0 || tx >= width || ty < 0 || ty >= height || map.getCell(tx, ty) == 1 || map.getCell(tx, ty) == 5 || vis[tx][ty] ) {
                    continue;
                }
                dis[tx][ty] = dis[cur.x][cur.y] + 1;
                vis[tx][ty] = true;
                nodes.add(new Node(tx, ty, cur.x, cur.y, cur.step + 1));
            }
        }

        int x = sx, y = sy;
        System.out.println(dx + " : " + dy);
        while (x != dx || y != dy) {
            for(int i = 0; i < 4; ++i) {
                int tx = x + dir[i][0];
                int ty = y + dir[i][1];
                if(tx < 0 || tx >= width || ty < 0 || ty >= height || map.getCell(tx, ty) == 1 || map.getCell(tx, ty) == 5) {
                    continue;
                }
                if(dis[x][y] == dis[tx][ty] + 1) {
                    directions.add(d[i]);
                //    System.out.println("cell : " +map.getCell(tx, ty));
                 //   ElementBean.Substance.getService().add(new Grass(tx * cellW, ty * cellW));
                    System.out.println(tx + " " + ty + " " + d[i].toString());
                    x = tx;
                    y = ty;
                    break;
                }
            }
//            System.out.println(x + " " + y + " -> " + dx + " " + dy);
        }
        System.out.println("end bfs");
        try{
            Thread.sleep(100 * 10000);
        }catch (Exception e) {

        }
    }

//    private boolean detectTarget() {
//        //int tx = enemy.getX() / cellW, ty = enemy.getY() / cellW;
//        //int dx = player.getX() / cellW, dy = enemy.getY() / cellW;
//
//        boolean detect = true;
//        if(tx == dx) {
//            int[] col = map.getCol(tx);
//            if(ty > dy) {
//                int t = ty;
//                ty = dy;
//                dy = t;
//            }
//            for(int i = ty; i < dy; ++i) {
//                if(col[i] == 1 || col[i] == 2) {
//                    detect = false;
//                    break;
//                }
//            }
//        }
//
//        if(ty == dy) {
//            int[] row = map.getRow(ty);
//            if(tx > dx) {
//                int t = tx;
//                tx = dx;
//                dx = t;
//            }
//            for(int i = tx; i < dx; ++i) {
//                if(row[i] == 1 || row[i] == 2) {
//                    detect = false;
//                    break;
//                }
//            }
//        }
//
//        return detect;
//    }

}
