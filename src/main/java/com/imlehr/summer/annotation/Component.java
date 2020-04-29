package com.imlehr.summer.annotation;

import java.lang.annotation.*;

/**
 * @author lehr
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

    /**
     * 如果没有就默认是类名开头小写，有的话就是作为id
     * @return
     */
    String value() default "";
}

