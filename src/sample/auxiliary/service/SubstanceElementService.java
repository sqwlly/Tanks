package sample.auxiliary.service;

import javafx.util.Pair;
import sample.auxiliary.Direction;
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

import java.awt.*;
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
        if (!myself.intersects(other)) return false;
        if (other instanceof Bullet) {
            //如果可移动物体的攻击力大于当前物体的防御力，就要减少生命值
            if (other.getAttack().getValue() > myself.getDefense().getValue()) {

                //砖瓦墙要特殊处理，因为有不同状态
                if (myself instanceof Tile) {
                    Tile tile = (Tile) myself;
                    for(int i = 0; i < tile.getRec().size(); ++i) {
                        //击中了四个角的一个角
                        if(tile.getRecState(i) == 1 && tile.getRec().get(i).intersects(other.getRectangle())) {
                            tile.hitState(i);
                            ((Bullet) other).hit();
//                            System.out.println("hit " + i);
                            //break;
                        }
                    }
                    boolean remove = true;
                    for(int i = 0; i < tile.getRec().size(); ++i) {
                        if(tile.getRecState(i) == 1) {
                            remove = false;
                        }
                    }
                    if(remove) {
                        other.die();
                    }
                }
                if (myself instanceof Home && !myself.getHp().health()) {
                    myself.getDefense().setValue(1000);
                }
                if(myself instanceof IBulletCross) {
                    myself.getDefense().setValue(0);
                }
//                System.out.println("抵消 " + (double) myself.getDefense().getValue() / (myself.getHp().getMaxValue() + myself.getDefense().getValue()) + " " +
//                        myself.getHp().getMaxValue());

                int subValue = (int) ((1 - (double) myself.getDefense().getValue() / myself.getHp().getMaxValue()) * other.getAttack().getValue());
//                System.out.println(subValue);
                myself.getHp().subtract(subValue);
//                System.out.println("restHP: " + myself.getHp().getValue());

            } else if (myself instanceof IBulletCross || myself instanceof Home) {
                return false;
            }
            if (myself instanceof Steel) {
                ((Bullet) other).hit();
                //玩家子弹击中钢铁播放音效
                if(((Bullet) other).getFrom() instanceof Player) {
                    Audio.bullet_hit_steel.play();
                }
            }
//            if(((Bullet) other).hitTarget()) {
//                System.out.println("bullet die");
//                ((Bullet) other).die(); //why??
//            }
            if (!myself.alive() && !(myself instanceof Home)) {
                this.remove(myself);
            }
            //终于找到bug在哪了！！！QAQ，泪目啊T_T
            //return true;
        }
        //如果可移动物体（本游戏只有坦克和子弹）与不能让坦克穿越的物体相交
        if (other instanceof Tank && !(myself instanceof ITankCross)) {

            /*************************************************/
            /*               坦克平滑移动核心代码！！           */

            // 主要目标是让坦克拐弯的时候流畅。

            // 基本思想是当坦克与障碍物碰撞时，从坦克坐标开始枚举其三分之一
            // 坦克宽度或者高度相对应的矩形，找到一个没有与障
            // 碍物碰撞的矩形，再将其坐标赋值即可

            Direction d = other.getDirection();
            Rectangle _try = new Rectangle(other.getRectangle());
            boolean ok = false;
            switch (d) {
                case UP:
                case DOWN:
                        for (int i = 1; i <= other.getWidth() / 3; ++i) {
                            _try.x = other.getX() + i;
                            if (!_try.intersects(myself.getRectangle())) {
                                other.setX(_try.x);
                                ok = true;
                                break;
                            }
                            _try.x = other.getX() - i;
                            if (!_try.intersects(myself.getRectangle())) {
                                other.setX(_try.x);
                                ok = true;
                                break;
                            }
                        }
                    break;
                case RIGHT:
                case LEFT:
                        for (int i = 1; i <= other.getHeight() / 3; ++i) {
                            _try.y = other.getY() - i;
                            if (!_try.intersects(myself.getRectangle())) {
                                ok = true;
                                other.setY(_try.y);
                                break;
                            }
                            _try.y = other.getY() + i;
                            if (!_try.intersects(myself.getRectangle())) {
                                other.setY(_try.y);
                                ok = true;
                                break;
                            }
                        }
                    break;
            }
            /*               坦克平滑移动核心代码！！           */
            /*************************************************/

            if(!ok) {
                ((Tank) other).stay(); //就要stay
            }
        }

        //坦克吃到道具
        if (myself instanceof Prop && other instanceof Tank) {
            Props props = ((Prop) myself).getProp();
            if(other instanceof Player) {
                Audio.get_bonus.play();
            }
            switch (props) {
                case Star:
                    if (other instanceof Player) {
                        ((Player) other).addLevel();
                        Audio.star.play();
                    } else {
                        //我知道这里或许可以改进的，两句基本一样的代码。。。改成Tank.addLevel()或许就可以了，但是太懒了，因为玩家和npc坦克等级并不一致
                        ((Enemy) other).addLevel();
                    }
                    break;
                case StopWatch:
                    if (other instanceof Player) {
                        ElementBean.Enemy.getService().getElementList().forEach(e -> {
                            if(e instanceof Tank) {
                                ((BaseElement) e).setActionTime(System.currentTimeMillis());
                                ((BaseElement) e).setStop(true);
                            }
                        });
                    }
                    break;
                case Bomb:
                    if (other instanceof Player) {
                        ElementBean.Enemy.getService().getElementList().forEach(e -> {
                            ((BaseElement) e).die();
                        });
                        //不知道为啥会报错啊。。。
//                        Audio.bonus_grenade.play();
                    } else {
                        ElementBean.Player.getService().getElementList().forEach(e -> {
                            if (e instanceof Player) {
                                ((Player) e).die();
                            }
                        });
                    }

                    break;
                case Tank:
                    if (other instanceof Player) {
                        int hearts = Integer.parseInt(Progress.getInstance().get("hearts")) + 1;
                        Progress.getInstance().set("hearts", hearts + "");
                        Audio.bonus_life.play();
                    }else{
                        other.getHp().add(80);
                    }
                    break;
                case Gun:
                    if (other instanceof Player) {
                        for (int i = 0; i < 3; ++i) {
                            ((Player) other).addLevel();
                        }
                        Audio.star.play();
                    } else {
                        //直接变成最终形态！
                        ((Enemy) other).setType(3);
                    }
                    break;
                case Iron_cap:
                    if (other instanceof Player) {
                        ((Player) other).beInvincible();
                    } else {
                        //加血不变相相当于加防御力嘛~
                        other.getHp().add(80);
                    }
                    break;
                case Spade:
                    //写的有点冗余，暂时想不到啥好办法
                    //先把eagle周围的墙壁坐标添加进list
                    ArrayList<Pair<Integer, Integer>> loc = new ArrayList<>();
                    for (int i = 0; i < 4; ++i) {
                        loc.add(new Pair<>(11 + i, 23));
                    }
                    for (int i = 0; i < 2; ++i) {
                        for (int j = 0; j < 2; ++j) {
                            loc.add(new Pair<>(11 + j * 3, 24 + i));
                        }
                    }
                    if (other instanceof Player) {
                        this.getElementList().forEach(e -> {
                            BaseElement element = (BaseElement) e;
                            //将eagle周围墙壁清除
                            for (Pair<Integer, Integer> pair : loc) {
                                if (pair.getKey() == element.getX() / 17 && pair.getValue() == element.getY() / 17) {
                                    this.remove(element);
                                }
                            }
                        });
                        //替换为钢铁墙壁
                        for (Pair<Integer, Integer> pair : loc) {
                            this.add(new Steel(pair.getKey() * 17, pair.getValue() * 17));
                            //System.out.println(pair.getKey() + " " + pair.getValue());
                        }
                    } else {
                        //清除eagle周围墙壁
                        this.getElementList().forEach(e -> {
                            BaseElement element = (BaseElement) e;
                            for (Pair<Integer, Integer> pair : loc) {
                                if (pair.getKey() == element.getX() / 17 && pair.getValue() == element.getY() / 17) {
                                    this.remove(element);
                                }
                            }
                        });
                    }
                    break;
            }
            //只有玩家才能得分
            if (other instanceof Player) {
                ((Player) other).addScore(500);
                ElementBean.Substance.getService().add(new Score(myself.getX() + 50, myself.getY(), 4));
            }
            //吃到道具之后就要将其移除
            this.remove(myself);
        }

        return super.intersectsHandle(myself, other);
    }
}
