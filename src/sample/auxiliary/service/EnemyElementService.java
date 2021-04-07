package sample.auxiliary.service;

import sample.base.BaseElement;
import sample.base.ElementService;
import sample.base.ITankCross;
import sample.base.Tank;
import sample.content.enemy.Enemy;
import sample.content.player.Player;
import sample.content.substance.Bullet;

public class EnemyElementService extends ElementService {
    @Override
    public void init() {
        super.init();
    }

    @Override
    protected boolean intersectsHandle(BaseElement myself, BaseElement other) {
        if(myself == other) return false;
        if(myself.intersects(other)) {
            if (other instanceof Bullet) {
                ((Bullet) other).boom();
                myself.subHp();
                return true;
            }
            if(myself instanceof Tank && other instanceof Tank) {
                ((Tank) myself).stay();
                ((Tank) other).stay();
            }
            if(myself instanceof Bullet) {
                ((Bullet) myself).boom();
                //如果敌人子弹击中玩家，那么想要玩家爆炸消失，就return true.
            }
        }
        return super.intersectsHandle(myself, other);
    }
}
