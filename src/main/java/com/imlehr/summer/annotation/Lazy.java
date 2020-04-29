package com.imlehr.summer.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author lehr
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface Lazy {


}
