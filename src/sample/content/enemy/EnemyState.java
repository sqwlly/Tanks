package sample.content.enemy;

import sample.auxiliary.*;

import java.util.Iterator;
import java.util.LinkedList;

public class EnemyState {

    private final LinkedList<IntelligentAI.Node> path;

    private final Enemy enemy;
    public EnemyState(Enemy enemy) {
        this.enemy = enemy;
        path = new LinkedList<>();
    }

    public void setPath(LinkedList<IntelligentAI.Node> path) {
        this.path.clear();
        this.path.addAll(path);
        //以下的写法会bug，导致所有坦克都会按照一个路线走！
//        this.path = path;
    }

    public void go(int step) {
        for(int i = 0; i < step; ++i) {
            enemy.move();
            enemy.stepReduce();
        }
    }

    public void AIMove() {
        if(enemy.getStep() == 0) {
            Iterator<IntelligentAI.Node> iterator = path.iterator();
            if(iterator.hasNext()) {
                IntelligentAI.Node node = iterator.next();
                iterator.remove();
                enemy.setDirection(node.dir);
                enemy.setStep(node.step);
                enemy.shoot();
            }
        }
        go(1);
    }

    public void simpleRandomMove() {
        enemy.move();
        enemy.stepReduce();
        if(enemy.getStep() <= 0) {
            int step = CommonUtils.nextInt(0, 50) + 10;
            enemy.setStep(step);
            Direction d = Direction.values()[CommonUtils.nextInt(0, 4)];
            enemy.setDirection(d);
            enemy.shoot();
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
