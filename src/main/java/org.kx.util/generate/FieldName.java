package org.kx.util.generate;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2021/1/8
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface FieldName {

    String value() default "";

    boolean Ignore() default false;
}