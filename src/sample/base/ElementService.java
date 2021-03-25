package sample.base;

import sample.auxiliary.Constant;
import sample.auxiliary.ElementBean;
import sample.content.player.Player;
import sample.content.substance.Bullet;
import sample.content.substance.BulletBoom;
import sample.content.substance.Steel;
import sample.content.substance.Tile;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ElementService<T extends BaseElement> extends BaseService<T> {
    public final <S extends BaseElement> void action(Player player, ElementService<S>... services) {
        this.getElementList().forEach(element -> {
            //根据元素自身判断其离开条件
            if (this.removeElement(element, player)) {
                if(element instanceof Player) return;
                if(element instanceof Bullet) {
                    player.getBullet().boom();
                }
                this.remove(element);
                return;
            }
//            //边距判定
//            boolean sideJudge = ElementBean.getBackground().sideJudge(element);
//            if (sideJudge) {
//                element.encounterSide();
//            }
            //前置操作
            if (!element.beforeActionJudge()) {
                return;
            }
            //与玩家相遇
            if (this.encounterPlayer(element, player)) {
                if(element instanceof Bullet) {
                    this.remove(element);
                }
                return;
            }

            //物质和子弹相遇
            if (this.encounterBullet(element, player.getBullet())) {
                if(!(element instanceof IBulletCross)) {
                    System.out.println("encounterBullet");
                    if(element instanceof Steel) {
                        return;
                    }
                    this.remove(element);
                }
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
                        if(serviceElement instanceof Tile) {
                            System.out.println("tile");
                        }
                        if (removeOther) {
                            System.out.println("remove");
                            service.remove(serviceElement);
                        }
                    }
                });
            }
            //动作
            element.action();
        });
    }

    protected boolean encounterBullet(T element, Bullet bullet) {
        return false;
    }

    /**
     * @Description 与玩家相遇之后
     * @Param [element, player]
     * @return boolean
     */
    protected boolean encounterPlayer(T element, Player player) {
        return element.encounterPlayer(player);
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
     * @Description 移除元素 当元素满足自身离开条件 或 当元素超过屏幕边界时移除
     * @Param [element, player]
     * @return boolean
     */
    protected boolean removeElement(T element, Player player) {
        return element.remove(player) || element.getY() > Constant.FRAME_HEIGHT
                || element.getX() < 0 || element.getY() < 0 || element.getX() > Constant.FRAME_HEIGHT;
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
