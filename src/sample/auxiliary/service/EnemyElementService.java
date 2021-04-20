package sample.auxiliary.service;

import sample.base.BaseElement;
import sample.base.ElementService;
import sample.content.common.Tank;
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
                other.die();
                int subValue = (int) ((double) myself.getDefense().getValue() / myself.getHp().getValue() * other.getAttack().getValue());
                myself.getHp().subtract(subValue);
                return true;
            }
            if(myself instanceof Tank && other instanceof Tank) {
                ((Tank) myself).stay();
                ((Tank) other).stay();
            }
            if(myself instanceof Bullet) {
                myself.die();
                //如果玩家不处于无敌状态
                if(!((Player) other).getInvincible().alive()) {
                    int subValue = (int) ((double) other.getDefense().getValue() / other.getHp().getValue() * myself.getAttack().getValue());
                    other.getHp().subtract(subValue);
                }
                //System.out.println(other.getHp().getValue());
            }
        }
        return super.intersectsHandle(myself, other);
    }
}
