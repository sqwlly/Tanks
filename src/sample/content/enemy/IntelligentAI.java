package sample.content.enemy;

import sample.auxiliary.*;
import sample.content.substance.Grass;
import sample.content.substance.Home;

import java.util.LinkedList;
import java.util.Queue;

public class IntelligentAI {
    static class Node{
        int x, y, step;
        Direction dir;
        public Node(int x, int y, int step, Direction d) {
            this.x = x;
            this.y = y;
            this.step = step;
            this.dir = d;
        }
    }
    private static final int[][] dir = new int[][]{{0,-1}, {1,0}, {0, 1}, {-1, 0}};
    private final Direction[] d = new Direction[] {Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT};

    private final LinkedList<Node> path = new LinkedList<>();
    private final static int cellW = Constant.ELEMENT_SIZE / 2;


    private final Map map;
    private final Home home;

    public IntelligentAI(Map map, GameStateManager gsm) {
        this.map = map;
        this.home = gsm.getHome();
    }

    private boolean ok(int tx, int ty) {
        int[][] cor = new int[][] {
                {tx, ty}, {tx + 1, ty}, {tx, ty + 1}, {tx + 1, ty + 1},
        };
        for(int[] p : cor) {
            if(p[0] >= 26 || p[1] >= 26 || p[0] < 0 || p[1] < 0) {
                continue;
            }
            int s = map.getCell(p[0], p[1]);
            if(s == 1 || s == 5) {
                return false;
            }
        }
        return true;
    }

    public void bfs(Enemy enemy) {
        if(home == null) return;
        path.clear();
//        System.out.println("start bfs");
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
//        System.out.println("sx = " + sx + " | sy = " + sy);
        int dx = home.getX() / cellW, dy = home.getY() / cellW;
        nodes.add(new Node(dx, dy, 0, enemy.getDirection()));
        vis[dx][dy] = true;
        dis[dx][dy] = 0;
        while(!nodes.isEmpty()) {
            Node cur = nodes.poll();
            for(int i = 0; i < 4; ++i) {
                int tx = cur.x + dir[i][0];
                int ty = cur.y + dir[i][1];
                if(tx < 0 || tx >= width || ty < 0 || ty >= height || !ok(tx, ty) || vis[tx][ty] ) {
                    continue;
                }
                dis[tx][ty] = dis[cur.x][cur.y] + 1;
                vis[tx][ty] = true;
                nodes.add(new Node(tx, ty, cur.step + 1, d[i]));
            }
        }

        int x = sx, y = sy;
//        System.out.println(dx + " : " + dy);
        while (x != dx || y != dy) {
            for (int i = 0; i < 4; ++i) {
                int tx = x + dir[i][0];
                int ty = y + dir[i][1];
                if (tx < 0 || tx >= width || ty < 0 || ty >= height || !ok(tx, ty)) {
                    continue;
                }
                if (dis[x][y] == dis[tx][ty] + 1) {
                    path.add(new Node(tx, ty, (int) Math.ceil((double) cellW / enemy.getSpeed()), d[i]));
//                    path.add(new Node(tx, ty,  cellW / enemy.getSpeed(), d[i]));

//                    System.out.println("cell : " + map.getCell(tx, ty));
//                    if(map.getCell(tx, ty) == 0) {
//                        map.setCell(tx, ty, 3);
//                        ElementBean.Substance?.getService().add(new Grass(tx * cellW, ty * cellW));
//                    }
//                    System.out.println(tx + " " + ty + " " + d[i].toString());
                    x = tx;
                    y = ty;
                    break;
                }
            }
//            System.out.println(x + " " + y + " -> " + dx + " " + dy);
        }
        enemy.getEnemyState().setPath(path);
        enemy.setEnemyMode(EnemyMode.INTELLIGENT);

//        System.out.println("end bfs");
//        t
//        ry{
//            Thread.sleep(100 * 10000);
//        }catch (Exception e) {
//
//        }
    }

}
