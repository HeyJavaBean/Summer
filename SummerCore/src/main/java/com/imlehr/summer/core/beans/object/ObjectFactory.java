package com.imlehr.summer.core.beans.object;


import com.imlehr.summer.core.annotation.Configuration;
import com.imlehr.summer.core.beans.definition.BeanDefinition;

/**
 * @author lehr
 * 这个是用来封装如何实例化一个对象的接口
 */
public abstract class ObjectFactory {

    public Object getObject()
    {
        if(instance==null)
        {
            getInstance();
        }
        return instance;
    }


    protected Object instance;

    protected BeanDefinition beanDefinition;

    public ObjectFactory(BeanDefinition beanDefinition)
    {
        this.beanDefinition = beanDefinition;
    }

    public static ObjectFactory getFactory(BeanDefinition beanDefinition)
    {
        if(beanDefinition.getMethod()!=null)
        {
            return new AnnotationBeanFactory(beanDefinition);
        }
        if(beanDefinition.getBeanClass().isAnnotationPresent(Configuration.class))
        {
            return new ScanBeanFactory(beanDefinition);
        }
        else
        {
            return new ScanBeanFactory(beanDefinition);
        }

    }

    public abstract void getInstance();


}
