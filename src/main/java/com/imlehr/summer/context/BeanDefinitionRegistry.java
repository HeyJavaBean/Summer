package com.imlehr.summer.context;

import com.imlehr.summer.beans.factory.BeanFactory;

/**
 * @author Lehr
 * @create: 2020-04-27
 * 这个接口代表的是对beanDefinition有控制能力的注册类的接口
 */
public interface BeanDefinitionRegistry {

    BeanFactory getBeanFactory();

}
