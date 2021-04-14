package sample.auxiliary.state;

import sample.auxiliary.GameStateManager;
import sample.auxiliary.Progress;
import sample.auxiliary.ResourceLoader;
import sample.base.IDraw;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

public abstract class GameState implements IDraw {
    protected Progress progress = Progress.getInstance();
    protected GameStateManager gsm;
    protected static Font font;

    static {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.loadFontStream("joystix.ttf")).deriveFont(20f);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void init();

    public abstract void mouseClicked(MouseEvent e);

    public abstract void stateAction();
}
