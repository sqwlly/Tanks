package sample.auxiliary.service;

import sample.auxiliary.ElementBean;
import sample.auxiliary.Progress;
import sample.base.*;
import sample.content.player.Player;
import sample.content.substance.Bullet;
import sample.content.substance.Home;
import sample.content.substance.Score;
import sample.content.substance.Steel;
import sample.content.substance.props.Prop;
import sample.content.substance.props.Props;

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
        if(other instanceof Bullet) {
            //如果可移动物体的攻击力大于当前物体的防御力，就要减少生命值
            if(other.getAttack().getValue() > myself.getDefense().getValue()) {
                int subValue = (int) ((double) myself.getDefense().getValue() / myself.getHp().getValue() * other.getAttack().getValue());
                myself.getHp().subtract(subValue);
            }else if(myself instanceof IBulletCross) {
                return false;
            }

            other.die(); //why??
            if(!myself.alive() && !(myself instanceof Home)) {
                this.remove(myself);
            }
            return true;
        }
        //如果可移动物体（本游戏只有坦克和子弹）与不能让坦克穿越的物体相交
        if(other instanceof Tank && !(myself instanceof ITankCross)) {
            ((Tank) other).stay(); //就要stay
        }

        //坦克吃到道具
        if(myself instanceof Prop && other instanceof Tank) {
            Props props = ((Prop) myself).getProp();
            switch (props) {
                case Star:
                    if(other instanceof Player) {
                        ((Player) other).addLevel();
                    }
                    break;
                case StopWatch:
                    if(other instanceof Player) {
                        ElementBean.Enemy.getService().getElementList().forEach(e -> {
                            ((BaseElement) e).setStop(true);
                        });
                    }
                    break;
                case Bomb:
                    if(other instanceof Player) {
                        ElementBean.Enemy.getService().getElementList().forEach(e -> {
                            ((BaseElement) e).die();
                        });
                    }
                    break;
                case Tank:
                    if(other instanceof Player) {
                        int hearts = Integer.parseInt(Progress.getInstance().get("hearts")) + 1;
                        Progress.getInstance().set("hearts", hearts + "");
                    }
                    break;
                case Gun:
                    if(other instanceof Player) {
                        for(int i = 0; i < 3; ++i) {
                            ((Player) other).addLevel();
                        }
                    }
                    break;
                case Iron_cap:
                    ((Player) other).beInvincible();
                    break;
                case Spade:

                    break;
            }
            //只有玩家才能得分
            if(other instanceof Player) {
                ((Player) other).addScore(500);
                ElementBean.Substance.getService().add(new Score(myself.getX() + 50, myself.getY(), 4));
            }
            //吃到道具之后就要将其移除
            this.remove(myself);
        }

        return super.intersectsHandle(myself, other);
    }
}
