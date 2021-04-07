package sample.auxiliary.service;

import sample.base.*;
import sample.content.substance.Bullet;
import sample.content.substance.Home;
import sample.content.substance.Steel;

public class SubstanceElementService extends ElementService {

    @Override
    public void init() {
        super.init();
    }

    public SubstanceElementService() {
    }

    @Override
    protected boolean intersectsHandle(BaseElement myself, BaseElement other) {
        if(!myself.intersects(other)) return false;
        if(other instanceof Bullet && !(myself instanceof IBulletCross)) {
            //other.subHp();
            //如果子弹可以击穿钢铁，或者被击中的物体不是钢铁，那么就要减少生命值
            if(((Bullet) other).isDestroy_Steel() || ! (myself instanceof Steel)) {
                myself.subHp();
            }
            ((Bullet) other).boom();

            if(!myself.alive() && !(myself instanceof Home)) {
                this.remove(myself);
            }
            return true;
        }
        //如果可移动物体（本游戏只有坦克和子弹）与不能让坦克穿越的物体相交
        if(other instanceof Tank && !(myself instanceof ITankCross)) {
            ((Tank) other).stay(); //就要stay
        }

        return super.intersectsHandle(myself, other);
    }
}
