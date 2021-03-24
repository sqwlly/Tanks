package sample.auxiliary.service;

import sample.auxiliary.Constant;
import sample.auxiliary.ElementBean;
import sample.auxiliary.graphics.TextureAtlas;
import sample.base.BaseElement;
import sample.base.ElementService;
import sample.base.IHurtPlayer;
import sample.content.player.Player;
import sample.content.substance.Born;
import sample.content.substance.Bullet;
import sample.content.substance.BulletBoom;
import sample.content.substance.Tile;

import java.util.ArrayList;
import java.util.List;

public class SubstanceElementService extends ElementService {

    public SubstanceElementService() {
    }

    @Override
    protected boolean encounterPlayer(BaseElement element, Player player) {
        if (element instanceof IHurtPlayer) {//陷阱统一处理
            if (element.intersects(player)) {
                player.beHurt();
                return true;
            }
        }
        return super.encounterPlayer(element, player);
    }

    @Override
    protected boolean encounterBullet(BaseElement element, Bullet bullet) {
        //这个位置后面要改
        if(!(element instanceof Tile)) return false;

        if(bullet != null && element.intersects(bullet)) {
            bullet.boom();
            return true;
        }
        return super.encounterBullet(element, bullet);
    }
}
