package sample.auxiliary;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public enum Keys {
    UP(KeyEvent.VK_W),
    DOWN(KeyEvent.VK_S),
    LEFT(KeyEvent.VK_A),
    RIGHT(KeyEvent.VK_D),
    PLAY2_UP(KeyEvent.VK_UP),
    PLAY2_DOWN(KeyEvent.VK_DOWN),
    PLAY2_LEFT(KeyEvent.VK_LEFT),
    PLAY2_RIGHT(KeyEvent.VK_RIGHT),
    ZERO(KeyEvent.VK_NUMPAD0),
    ENTER(KeyEvent.VK_ENTER),
    SPACE(KeyEvent.VK_SPACE);

    private int keyValue;
    private static Set<Integer> keySet = new HashSet<>();//按键
    Keys(int keyValue) {
        this.keyValue = keyValue;
    }

    /**
     * @Description 是否使用了某键
     * @Param []
     * @return boolean
     */
    public boolean use() {
        return keySet.contains(keyValue);
    }

    /**
     * @Description 添加按键
     * @Param [keyCode]
     * @return void
     */
    public static void add(int keyCode) {
        keySet.add(keyCode);
    }

    /**
     * @Description 移除按键
     * @Param [keyCode]
     * @return void
     */
    public static void remove(int keyCode) {
        keySet.remove(keyCode);
    }
}
