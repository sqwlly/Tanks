package sample.base;

import sample.auxiliary.Constant;
import sample.auxiliary.ElementBean;
import sample.content.enemy.Enemy;
import sample.content.player.Player;
import sample.content.substance.*;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ElementService<T extends BaseElement> extends BaseService<T> {
    private Player player;
    public final <S extends BaseElement> void action(Player player, ElementService<S>... services) {
        this.getElementList().forEach(element -> {
            if(element.remove(player)) {
                this.remove(element);
                return;
            }

            if(element instanceof Home) {
                if(!element.alive()) {
                    player.subHp();
                }
            }

            //前置操作
            if (!element.beforeActionJudge()) {
                return;
            }

            //与其它元素相交处理
            Rectangle myself = element.getRectangle();
            for (ElementService service : services) {
                //获取交互元素服务列表
                CopyOnWriteArrayList<S> elementList = service.getElementList();
                elementList.forEach(serviceElement -> {
                    //如果与矩形相交
                    if (myself.intersects(serviceElement.getRectangle())) {
                        //相交处理 处理结束之后是否把对方从对方的元素列表中移除
                        boolean removeOther = this.intersectsHandle(element, serviceElement);
                        if (removeOther) {
//                            serviceElement.remove(serviceElement);
//                            System.out.println("remove");
                            service.remove(serviceElement);
                        }
                    }
                });
            }
            //动作
            element.action();
        });
    }

    /**
     * @Description 相交处理 结束之后是否移除对方
     * @Param [myself, other]
     * @return boolean
     */
    protected <S extends BaseElement> boolean intersectsHandle(T myself, S other) {
        return false;
    }

    /**
     * @Description 绘制
     * @Param [g]
     * @return void
     */
    @Override
    public final void drawImage(Graphics g) {
        this.getElementList().forEach(i -> i.drawImage(g));
    }

    /**
     * @Description 跟随玩家移动
     * @Param [player]
     * @return void
     */
    public final void movedByPlayer(Player player) {
        this.getElementList().forEach(i -> {
            i.setX((int) (i.getX() - player.getSpeed()));
            i.setY((int) (i.getY() - player.getSpeed()));
        });
    }

    @Override
    public void add(T element) {
        super.add(element);
    }

    @Override
    public void remove(T element) {
        super.remove(element);
    }
}
