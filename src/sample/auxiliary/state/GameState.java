package sample.auxiliary.state;

import sample.auxiliary.GameStateManager;
import sample.auxiliary.Progress;
import sample.base.IDraw;

import java.awt.event.MouseEvent;

public abstract class GameState implements IDraw {
    protected Progress progress;
    protected GameStateManager gsm;
    public abstract void init();

    public abstract void mouseClicked(MouseEvent e);

}
