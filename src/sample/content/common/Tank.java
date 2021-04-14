package sample.content.common;

import sample.auxiliary.ElementBean;
import sample.auxiliary.Progress;
import sample.base.BaseElement;
import sample.base.IMovable;
import sample.content.enemy.Enemy;
import sample.content.player.Player;
import sample.content.substance.Born;
import sample.content.substance.Score;
import sample.content.substance.TankBoom;

public abstract class Tank extends BaseElement implements IMovable {
    protected Born born;
    protected int oldX, oldY;
    protected int bulletNum;

    public void bulletNumAdd() {
        bulletNum++;
    }

    public void bulletNumInit() {
        bulletNum = 1;
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
        //这样写似乎会增加一些与其他类的耦合度
        if (!alive()) {
            //敌人死亡就可以给玩家增加分数
            if (this instanceof Enemy && element instanceof Player) {
                ((Player) element).addScore(((Enemy) this).getReward());
                int type = ((Enemy) this).getType();
                //将得分元素加入绘画列表
                ElementBean.Substance.getService().add(new Score(x + width * 2, y, type));
                //单例模式进行分数和击杀计算
                int cnt = Integer.parseInt(Progress.getInstance().get("killed" + (type + 1))) + 1;
                int rest = Integer.parseInt(Progress.getInstance().get("restEnemy")) - 1;
                Progress.getInstance().set("restEnemy", rest + "");
                Progress.getInstance().set("killed" + (type + 1), cnt + "");
                Progress.getInstance().set("currentScore", ((Player) element).getScore() + "");
            }
            //将坦克爆炸元素加入绘画列表
            ElementBean.Substance.getService().add(new TankBoom(x - width / 2, y - height / 2));
            if(this instanceof Player) {
                return false;
                //System.out.println(this.hp.getValue());
            }

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
