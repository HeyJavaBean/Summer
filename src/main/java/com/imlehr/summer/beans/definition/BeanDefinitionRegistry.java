package com.imlehr.summer.beans.definition;

import com.imlehr.summer.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Lehr
 * @create: 2020-04-27
 * 这个接口代表的是对beanDefinition有控制能力的注册类的接口
 */
public interface BeanDefinitionRegistry {

    BeanFactory getBeanFactory();

    void scan(String... basePackages);

    void register(Class... componentClasses);

    void registBean(List<Method> beans,Object configBean);

}
