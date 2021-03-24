package sample.content.substance;

import sample.auxiliary.Direction;
import sample.base.IHurtPlayer;

public class EnemyBullet extends Bullet implements IHurtPlayer {
    public EnemyBullet(int x, int y, Direction direction) {
        super(x, y, direction);
    }
}
