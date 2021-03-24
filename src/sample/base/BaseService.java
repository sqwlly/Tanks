package sample.base;

import sample.content.player.Player;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class BaseService<T> implements IBaseService<T> {
    private CopyOnWriteArrayList<T> elementList = new CopyOnWriteArrayList<>();//元素列表

    @Override
    public void init() {
        elementList.clear();
    }

    @Override
    public void add(T element) {
        this.elementList.add(element);
    }

    @Override
    public void remove(T element) {
        this.elementList.remove(element);
    }

    @Override
    public CopyOnWriteArrayList<T> getElementList() {
        return elementList;
    }

    @Override
    public void drawImage(Graphics g) {

    }

    /**
     * @Description 跟随玩家移动
     * @Param [player]
     * @return void
     */
    @Override
    public void movedByPlayer(Player player) {

    }
}
