package sample.auxiliary.service;

import javafx.util.Pair;
import sample.auxiliary.ElementBean;
import sample.auxiliary.Progress;
import sample.base.*;
import sample.content.common.Tank;
import sample.content.player.Player;
import sample.content.substance.*;
import sample.content.substance.props.Prop;
import sample.content.substance.props.Props;

import java.util.ArrayList;

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
                    }else{
                        ElementBean.Player.getService().getElementList().forEach(e -> {
                            if(e instanceof Player) {
                                ((Player) e).die();
                            }
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
                    if(other instanceof  Player) {
                        ((Player) other).beInvincible();
                    }
                    break;
                case Spade:
                    //too long!
                    //先把eagle周围的墙壁坐标添加进list
                    ArrayList<Pair<Integer, Integer>> loc = new ArrayList<>();
                    for(int i = 0; i < 4; ++i) {
                        loc.add(new Pair<>(11 + i, 23));
                    }
                    for(int i = 0; i < 2; ++i) {
                        for(int j = 0; j < 2; ++j) {
                            loc.add(new Pair<>(11 + j * 3, 24 + i));
                        }
                    }
                    if(other instanceof Player) {
                        this.getElementList().forEach(e -> {
                            BaseElement element = (BaseElement) e;
                            //将eagle周围墙壁清除
                            for(Pair<Integer, Integer> pair : loc) {
                                if(pair.getKey() == element.getX() / 17 && pair.getValue() == element.getY() / 17) {
                                    this.remove(element);
                                }
                            }
                        });
                        //替换为钢铁墙壁
                        for(Pair<Integer, Integer> pair : loc) {
                            this.add(new Steel(pair.getKey() * 17, pair.getValue() * 17));
                            //System.out.println(pair.getKey() + " " + pair.getValue());
                        }
                    }else{
                        //清除eagle周围墙壁
                        this.getElementList().forEach(e -> {
                            BaseElement element = (BaseElement) e;
                            for(Pair<Integer, Integer> pair : loc) {
                                if(pair.getKey() == element.getX() / 17 && pair.getValue() == element.getY() / 17) {
                                    this.remove(element);
                                }
                            }
                        });
                    }
                    break;
            }
            //只有玩家才能得分
            if(other instanceof Player) {
                ((Player) other).addScore(500);
                ElementBean.Substance.getService().add(new Score(myself.getX() + 50, myself.getY(), 4));
            }
            //吃到道具之后就要将其移除
            if(other instanceof  Player)
            this.remove(myself);
        }

        return super.intersectsHandle(myself, other);
    }
}
