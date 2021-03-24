package sample.base;

import sample.content.player.Player;

import java.util.concurrent.CopyOnWriteArrayList;

public interface IBaseService<T> extends IDraw {

    /**
     * @Description 初始化元素列表
     * @Param []
     * @return void
     */
    void init();

    /**
     * @Description 添加元素
     * @Param [element]
     * @return void
     */
    void add(T element);

    /**
     * @Description 移除元素
     * @Param [element]
     * @return void
     */
    void remove(T element);

    /**
     * @Description 获取元素列表
     * @Param []
     * @return java.util.concurrent.CopyOnWriteArrayList<T>
     */
    CopyOnWriteArrayList<T> getElementList();

    /**
     * @Description 跟随玩家移动
     * @Param [player]
     * @return void
     */
    void movedByPlayer(Player player);
}
