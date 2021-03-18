package sample.base;

import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.content.player.Player;

import java.awt.*;

public abstract class BaseElement implements IDraw{
    protected int x, y;
    protected int width, height;
    protected int speed;
    protected Direction direction;

    public BaseElement() {
        this.width = Constant.ELEMENT_SIZE;
        this.height = Constant.ELEMENT_SIZE;
        this.direction = Direction.LEFT;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public BaseElement(int x, int y) {
        IElement ann = this.getClass().getAnnotation(IElement.class);
        this.x = x;
        this.y = y;
        this.speed = ann.speed();
        this.width = ann.width();
        this.height = ann.height();
        this.direction = ann.direction();
    }

    public void action() {
        move();
    }

    protected void move() {
        if(direction.up()) {
            this.y -= speed;
        }else if(direction.down()) {
            this.y += speed;
        }else if(direction.right()) {
            this.x += speed;
        }else if(direction.left()) {
            this.x -= speed;
        }
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, width, height);
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
     * @Param [player]
     * @return boolean
     */
    public boolean remove(Player player) {
        return false;
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
}
