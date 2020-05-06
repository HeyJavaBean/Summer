package com.imlehr.summer.annotation;

import com.imlehr.summer.annotation.scan.Component;

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
