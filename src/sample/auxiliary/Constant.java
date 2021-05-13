package sample.auxiliary;

public abstract class Constant {
    public static boolean TIMER_STOP_ON_OFF = false;//定时器结束总开关，打开时停止所有定时器的刷新

    public final static int ELEMENT_SIZE = 34;//素材原始尺寸
    public final static int FRAME_WIDTH = ELEMENT_SIZE * 15 + 1;//主窗体宽
    public final static int FRAME_HEIGHT = ELEMENT_SIZE * 14 - 6;//主窗体高
    public final static int GAME_WIDTH = ELEMENT_SIZE * 13;
    public final static int GAME_HEIGHT = ELEMENT_SIZE * 13;
    public static final String ATLAS_FILE_NAME	= "images/tank_sprite.png";
//    public static final String ATLAS_FILE_NAME	= "images/texture_atlas.png";


}
