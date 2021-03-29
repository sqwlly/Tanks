package sample.content.enemy;

import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.auxiliary.Map;
import sample.base.BaseElement;
import sample.content.player.Player;
import sample.content.substance.Home;

public class EnemyState {

    private final static int cellW = Constant.ELEMENT_SIZE / 2;

    private Enemy enemy;
    private Map map;
    private Player player;
    public EnemyState(Enemy enemy, Player player, Map map) {
        this.enemy = enemy;
        this.map = map;
        this.player = player;
    }

    public void dfs(Home home) {
        int tx = enemy.getX() / (cellW * 2), ty = enemy.getY() / (cellW * 2);
        int t = (tx - 6) * (tx - 6) + (ty - 12) * (ty - 12);
        int d = (int)Math.sqrt(t);
        if(d > 0) {

        }
    }

    public void bfs() {

    }

    private boolean detectTarget() {
        int tx = enemy.getX() / cellW, ty = enemy.getY() / cellW;
        int dx = player.getX() / cellW, dy = enemy.getY() / cellW;

        int[][] dir = new int[][]{{0,-1}, {1,0}, {0, 1}, {-1, 0}};

        boolean detect = true;
        if(tx == dx) {
            int[] col = map.getCol(tx);
            if(ty > dy) {
                int t = ty;
                ty = dy;
                dy = t;
            }
            for(int i = ty; i < dy; ++i) {
                if(col[i] == 1 || col[i] == 2) {
                    detect = false;
                    break;
                }
            }
        }

        if(ty == dy) {
            int[] row = map.getRow(ty);
            if(tx > dx) {
                int t = tx;
                tx = dx;
                dx = t;
            }
            for(int i = tx; i < dx; ++i) {
                if(row[i] == 1 || row[i] == 2) {
                    detect = false;
                    break;
                }
            }
        }

        return detect;
    }

}
