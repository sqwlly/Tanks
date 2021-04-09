package sample.base;

import sample.auxiliary.Constant;
import sample.auxiliary.Direction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IElement {
    int width() default Constant.ELEMENT_SIZE;//宽度

    int height() default Constant.ELEMENT_SIZE;//高度

    Direction direction() default Direction.UP;

    int hp() default 50;//血量

    int speed() default 3;//速度

    int attack() default 0; //攻击力

    int defense() default 50; //防御力
}
