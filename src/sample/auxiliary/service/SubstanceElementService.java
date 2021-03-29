package sample.auxiliary.service;

import sample.auxiliary.ElementBean;
import sample.base.*;
import sample.content.enemy.Enemy;
import sample.content.player.Player;
import sample.content.substance.Bullet;
import sample.content.substance.EnemyBullet;
import sample.content.substance.Steel;
import sample.content.substance.Water;

public class SubstanceElementService extends ElementService {

    public SubstanceElementService() {
    }

//    @Override
//    protected boolean encounterPlayer(BaseElement element, Player player) {
//        if (element instanceof IHurtPlayer) {//陷阱统一处理
//            if (element.intersects(player)) {
//                player.beHurt();
//                return true;
//            }
//        }
//        if(!(element instanceof ITankCross)) {
//            if(element.intersects(player)) {
//                player.stay();
//                return true;
//            }
//        }
//        return super.encounterPlayer(element, player);
//    }

    @Override
    protected boolean intersectsHandle(BaseElement myself, BaseElement other) {
        if(!myself.intersects(other)) return false;
        if(other instanceof Bullet && !(myself instanceof IBulletCross)) {
            //other.subHp();
            if(((Bullet) other).isDestroy_Steel() || ! (myself instanceof Steel)) {
                myself.subHp();
            }
            ((Bullet) other).boom();
            if(!myself.alive()) {
                this.remove(myself);
            }
            return true;
        }
        //如果可移动物体（本游戏只有坦克和子弹，或者说实现了IMovable接口）与不能让坦克穿越的物体相交
        if(other instanceof IMovable && !(myself instanceof ITankCross)) {
            ((IMovable) other).stay(); //就要stay
        }

        return super.intersectsHandle(myself, other);
    }

//    @Override
//    protected boolean encounterBullet(BaseElement element, Bullet bullet) {
//        //子弹可以穿过的话就返回false
//        if(element instanceof IBulletCross) return false;
//
//        if(element.intersects(bullet)) {
//            bullet.boom();
//            return true;
//        }
//        return super.encounterBullet(element, bullet);
//    }
}
