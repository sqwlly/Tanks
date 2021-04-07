package sample.auxiliary.service;

import sample.base.BaseElement;
import sample.base.ElementService;
import sample.content.player.Player;

public class PlayerElementService extends ElementService {
    @Override
    public void init() {
        super.init();
    }

    @Override
    protected boolean intersectsHandle(BaseElement myself, BaseElement other) {
        if(!myself.intersects(other)) return false;
        return super.intersectsHandle(myself, other);
    }
}
