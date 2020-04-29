package com.imlehr.summer.beans;

import jdk.internal.jline.internal.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;

/**
 * @author Lehr
 * @create: 2020-04-27
 * 并不是太能理解为什么要设置这个BeanDefinitionHolder再来包装一次？？？
 * FIXME
 * 他官方注解给的解释是：If you don't care about BeanNameAware and the like,
 */
@Data
@Accessors(chain = true)
public class BeanDefinitionHolder {

    private BeanDefinition beanDefinition;

    private String beanName;

    @Nullable
    private String[] aliases;



}
