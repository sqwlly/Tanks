package sample.auxiliary.service;

import sample.base.*;
import sample.content.player.Player;
import sample.content.substance.Bullet;
import sample.content.substance.Water;

public class SubstanceElementService extends ElementService {

    public SubstanceElementService() {
    }

    @Override
    protected boolean encounterPlayer(BaseElement element, Player player) {
        if (element instanceof IHurtPlayer) {//陷阱统一处理
            if (element.intersects(player)) {
                player.beHurt();
                return true;
            }
        }
        if(!(element instanceof ITankCross)) {
            if(element.intersects(player)) {
                player.stay();
                return true;
            }
        }
        return super.encounterPlayer(element, player);
    }

    @Override
    protected boolean encounterBullet(BaseElement element, Bullet bullet) {
        //子弹可以穿过的话就返回false
        if(element instanceof IBulletCross) return false;

        if(element.intersects(bullet)) {
//            System.out.println(element.toString());
//            System.out.println(bullet.toString());
            bullet.boom();
            return true;
        }
        return super.encounterBullet(element, bullet);
    }
}
