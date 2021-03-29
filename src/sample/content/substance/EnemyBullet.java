package sample.content.substance;

import sample.auxiliary.Constant;
import sample.auxiliary.Direction;
import sample.auxiliary.ElementBean;
import sample.base.BaseElement;
import sample.base.IElement;
import sample.base.IHurtPlayer;

@IElement(width = 9, height = 10, speed = 6)
public class EnemyBullet extends Bullet implements IHurtPlayer {
    public EnemyBullet(int x, int y, Direction direction) {
        super(x, y, direction);
    }

    @Override
    public void action() {
        super.move();
//        System.out.println(x + " " + y +  " move");
        //boom();
    }

    @Override
    public void boom() {
//       // System.out.println("enemyBullet boom");
//      //  ElementBean.Substance.getService().add(new BulletBoom(x - 17 + 5, y - 17 + 5));
//      //  ElementBean.Enemy.getService().remove(this);
////        ElementBean.Enemy.getService().getElementList().forEach(e -> {
////            if(e instanceof EnemyBullet) {
////                System.out.println(e.toString());
////            }
////        });
//        System.out.println(ElementBean.Enemy.getService().getElementList().size());
//        this.setX(-Constant.FRAME_WIDTH);
//        this.subHp();
//        System.out.println("enemyBullet "+alive());
    }
}
