package com.imlehr.summer.annotation;

import java.lang.annotation.*;


/**
 * @author lehr
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    /**
     * 默认按照类型注入，true代表没有的话能不能是null
     * @return
     */
    boolean required() default true;
}

