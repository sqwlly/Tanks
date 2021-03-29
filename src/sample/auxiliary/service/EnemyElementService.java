package sample.auxiliary.service;

import sample.base.BaseElement;
import sample.base.ElementService;
import sample.base.ITankCross;
import sample.content.enemy.Enemy;
import sample.content.player.Player;
import sample.content.substance.Bullet;

public class EnemyElementService extends ElementService {
    @Override
    protected boolean intersectsHandle(BaseElement myself, BaseElement other) {
        if(myself.intersects(other)) {
            if (other instanceof Bullet) {
                ((Bullet) other).boom();
                myself.subHp();
                return true;
            }
        }
        return super.intersectsHandle(myself, other);
    }
}
