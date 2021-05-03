package sample.auxiliary.service;

import sample.auxiliary.Direction;
import sample.auxiliary.audio.Audio;
import sample.base.BaseElement;
import sample.base.ElementService;
import sample.content.common.Tank;
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
                other.die();
//                System.out.println((double) myself.getDefense().getValue() / myself.getHp().getMaxValue() + " " +
//                        myself.getHp().getMaxValue());
                int subValue = (int) ((1 - (double) myself.getDefense().getValue() / myself.getHp().getMaxValue()) * other.getAttack().getValue());
//                System.out.println("subValue: " + subValue);
                myself.getHp().subtract(subValue);
                System.out.println("restHP: " + myself.getHp().getValue());
                if(myself instanceof Tank && ((Enemy) myself).getType() >= 3 && myself.alive()) {
                    Audio.bullet_hit_steel.play();
                }

                return true;
            }
            if(myself instanceof Tank && other instanceof Tank) {
                ((Enemy) myself).stay();
                ((Tank) other).stay();
                //啊，杀了我吧！
//                boolean stay = false;
//                switch (myself.getDirection()) {
//                    case LEFT:
//                        if(other.getDirection() == Direction.RIGHT) {
//                            stay = true;
//                        }else if(other.getDirection() == Direction.UP || other.getDirection() == Direction.DOWN) {
//                            ((Tank) other).stay();
//                        }
//                        break;
//                    case RIGHT:
//                        if(other.getDirection() == Direction.LEFT) {
//                            stay = true;
//                        }else if(other.getDirection() == Direction.UP || other.getDirection() == Direction.DOWN) {
//                            ((Tank) other).stay();
//                        }
//                        break;
//                    case UP:
//                        if(other.getDirection() == Direction.DOWN) {
//                            stay = true;
//                        }else if(other.getDirection() == Direction.LEFT || other.getDirection() == Direction.RIGHT) {
//                            ((Tank) other).stay();
//                        }
//                        break;
//                    case DOWN:
//                        if(other.getDirection() == Direction.UP) {
//                            stay = true;
//                        }else if(other.getDirection() == Direction.LEFT || other.getDirection() == Direction.RIGHT) {
//                            ((Tank) other).stay();
//                        }
//                        break;
//                }
//                if(stay) {
//                    ((Tank) myself).stay();
//                    ((Tank) other).stay();
//                }
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
