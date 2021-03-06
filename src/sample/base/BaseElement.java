package sample.base;

import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.auxiliary.ElementBean;
import sample.content.common.Attribute;
import sample.content.player.Player;
import sample.content.substance.TankBoom;

import java.awt.*;

public abstract class BaseElement implements IDraw{
    protected int x, y;
    protected int width, height;
    protected Direction direction;

    public long getActionTime() {
        return actionTime;
    }

    public void setActionTime(long actionTime) {
        this.actionTime = actionTime;
    }

    protected long actionTime;

    public Attribute getAttack() {
        return attack;
    }

    public Attribute getDefense() {
        return defense;
    }

    protected Attribute hp;
    protected Attribute attack;
    protected Attribute defense;
    protected Attribute speed;

    public BaseElement() {
        this.width = Constant.ELEMENT_SIZE;
        this.height = Constant.ELEMENT_SIZE;
        this.direction = Direction.LEFT;
    }

    public int getSpeed() {
        return speed.getValue();
    }

    public void setSpeed(int speed) {
        this.speed.setValue(speed);
    }

    protected boolean stop;

    public BaseElement(int x, int y) {
        IElement ann = this.getClass().getAnnotation(IElement.class);
        this.x = x;
        this.y = y;
        this.width = ann.width();
        this.height = ann.height();
        this.direction = ann.direction();
        this.hp = new Attribute(ann.hp(), ann.hp());
        this.attack = new Attribute(ann.attack());
        this.defense = new Attribute(ann.defense());
        this.speed = new Attribute(ann.speed());
        this.stop = false;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public void action() {

    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
    }
    
    /**
     * @Description 动作前判定 服务列表中调用 通过重写该方法，为元素的某个动作进行判断
     * @Param []
     * @return boolean
     */
    public boolean beforeActionJudge() {
        return true;
    }

    /**
     * @Description 遇到边界或者墙面 结束之后是否移除
     * @Param []
     * @return void
     */
    public boolean encounterSide() {
        if(x - speed.getValue() <= 0 || x + speed.getValue() >= Constant.GAME_WIDTH || y - speed.getValue() <= 0 || y  + speed.getValue() >= Constant.FRAME_HEIGHT) {
            return true;
        }
        return false;
    }
    
    /**
     * @Description 是否与指定矩形相交
     * @Param [element]
     * @return boolean
     */
    public <E extends BaseElement> boolean intersects(E element) {
        return this.getRectangle().intersects(element.getRectangle());
    }

    /**
     * @Description 当元素自身满足离开条件时，从服务列表移除
     * @Param [element]
     * @return boolean
     */
    public boolean remove(BaseElement element) {
        return false;
    }

    public void die() {
        this.hp.setValue(0);
    }

    public Attribute getHp() {
        return hp;
    }

    public boolean alive() {
        return hp.getValue() > 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
