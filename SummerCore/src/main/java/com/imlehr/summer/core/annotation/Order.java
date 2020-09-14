package com.imlehr.summer.core.annotation;

import java.lang.annotation.*;

/**
 * @author lehr
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface Order {

    /**
     * 默认是最低优先级,值越小优先级越高
     */
    int value() default Ordered.LOWEST_PRECEDENCE;

}

