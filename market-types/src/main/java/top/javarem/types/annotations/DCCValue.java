package top.javarem.types.annotations;

import java.lang.annotation.*;

/**
 * @Author: rem
 * @Date: 2025/01/24/18:02
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface DCCValue {

    String value() default "";

}
