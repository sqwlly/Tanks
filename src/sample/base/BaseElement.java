package sample.base;

import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.content.player.Player;

import java.awt.*;

public abstract class BaseElement implements IDraw{
    protected int x, y;
    protected int width, height;
    protected int speed;
    protected int hp;
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
        this.hp = ann.hp();
    }

    public void action() {
    }

//    protected void move() {
//        if(direction.up()) {
//            this.y -= speed;
//        }else if(direction.down()) {
//            this.y += speed;
//        }else if(direction.right()) {
//            this.x += speed;
//        }else if(direction.left()) {
//            this.x -= speed;
//        }
//    }

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
     * @Description 遇到玩家处理 结束之后是否移除
     * @Param [player]
     * @return boolean
     */
    public boolean encounterPlayer(Player player) {
        return false;
    }
    
    /**
     * @Description 遇到边界或者墙面 结束之后是否移除
     * @Param []
     * @return void
     */
    public boolean encounterSide() {
        if(x - speed <= 0 || x + speed >= Constant.FRAME_WIDTH || y - speed <= 0 || y  + speed >= Constant.FRAME_HEIGHT) {
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
        this.hp = 0;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public void subHp() {
        hp -= 50;
    }

    public boolean alive() {
        return hp > 0;
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
