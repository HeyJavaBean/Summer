package com.imlehr.summer.core.annotation;

import com.imlehr.summer.core.annotation.scan.Component;

import java.lang.annotation.*;

/**
 * @author lehr
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {

    String value() default "";
}
