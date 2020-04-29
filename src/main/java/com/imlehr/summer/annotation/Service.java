package com.imlehr.summer.annotation;

import java.lang.annotation.*;

/**
 * @author lehr
 * todo 改成用标签修饰 也就是上面直接加一个component那个
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {

    /**
     * 如果没有就默认是类名开头小写，有的话就是作为id
     * @return
     */
    String value() default "";
}

