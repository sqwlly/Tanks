package sample.auxiliary.service;

import javafx.util.Pair;
import sample.auxiliary.ElementBean;
import sample.auxiliary.Progress;
import sample.auxiliary.audio.Audio;
import sample.base.*;
import sample.content.common.Tank;
import sample.content.enemy.Enemy;
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
                if(myself instanceof Home) {
                    myself.getDefense().setValue(1000);
                }
                int subValue = (int) ((double) myself.getDefense().getValue() / myself.getHp().getValue() * other.getAttack().getValue());
                myself.getHp().subtract(subValue);
            }else if(myself instanceof IBulletCross || myself instanceof Home) {
                return false;
            }
            //玩家子弹击中钢铁播放音效
            if(myself instanceof Steel && ((Bullet) other).getFrom() instanceof Player) {
                Audio.bullet_hit_steel.play();
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
            Audio.get_bonus.play();
            switch (props) {
                case Star:
                    if(other instanceof Player) {
                        ((Player) other).addLevel();
                        Audio.star.play();
                    }else{
                        //我知道这里或许可以改进的，两句基本一样的代码。。。改成Tank.addLevel()或许就可以了，但是太懒了，因为玩家和npc坦克等级并不一致
                        ((Enemy) other).addLevel();
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
                        //不知道为啥会报错啊。。。
                        Audio.bonus_grenade.play();
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
                        Audio.bonus_life.play();
                    }
                    break;
                case Gun:
                    if(other instanceof Player) {
                        for(int i = 0; i < 3; ++i) {
                            ((Player) other).addLevel();
                        }
                        Audio.star.play();
                    }else{
                        //直接变成最终形态！
                        ((Enemy) other).setType(3);
                    }
                    break;
                case Iron_cap:
                    if(other instanceof  Player) {
                        ((Player) other).beInvincible();
                    }else{
                        //加血不变相相当于加防御力嘛~
                        other.getHp().add(50);
                    }
                    break;
                case Spade:
                    //写的有点冗余，暂时想不到啥好办法
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
            this.remove(myself);
        }

        return super.intersectsHandle(myself, other);
    }
}
