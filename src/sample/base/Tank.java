package sample.base;

import sample.auxiliary.Constant;
import sample.auxiliary.ElementBean;
import sample.content.substance.Born;
import sample.content.substance.Bullet;
import sample.content.substance.TankBoom;

import java.util.List;

public abstract class Tank extends BaseElement implements IMovable{
    protected Born born;
    protected int oldX, oldY;
    protected int bulletNum;

    public void bulletNumAdd() {
        bulletNum++;
    }

    public Tank(int x, int y) {
        super(x, y);
        born = new Born(x, y);
    }

    public boolean fireAble() {
        return bulletNum > 0;
    }

    @Override
    public boolean remove(BaseElement element) {
        if(!alive()) {
            ElementBean.Substance.getService().add(new TankBoom(x - width / 2, y - height / 2));
            return true;
        }
        return super.remove(element);
    }

    public void shoot() {
    }

    public void stay() {
        x = oldX;
        y = oldY;
    }
}
