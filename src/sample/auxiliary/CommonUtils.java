package sample.auxiliary;

import sample.base.ITimer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public abstract class CommonUtils {
    private static final Random RANDOM = new Random(); //随机数

    /**
     * @Description 获取整形随机数 不判断开始范围于结束范围
     * @Param  [start, end]
     * @return int
     */
    public static int nextInt(int start, int end) {
        return start == end ? start : start + RANDOM.nextInt(end - start);
    }

    /**
     * @Description 开启一个指定频率的定时器
     * @Param [period, t]
     * @return void
     */
    public static void task(long period, ITimer t) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //当结束开关打开时，清除所有定时器
                if(Constant.TIMER_STOP_ON_OFF) {
                    timer.cancel();
                    return;
                }
                t.run();
            }
        };
        timer.schedule(timerTask, 0, period);
    }
}
