package com.imlehr.summer.core.beans.object;

import com.imlehr.summer.core.beans.definition.BeanDefinition;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;

/**
 * @author Lehr
 * @create: 2020-04-30
 */
public class ScanBeanFactory extends ObjectFactory {



    public ScanBeanFactory(BeanDefinition beanDefinition)
    {
        super(beanDefinition);
    }

    @Override
    @SneakyThrows
    public void getInstance() {
        Constructor constructor = beanDefinition.getBeanClass().getConstructor(beanDefinition.getParams());
        Object bean = constructor.newInstance(beanDefinition.getArgs());
        instance = bean;
    }
}
