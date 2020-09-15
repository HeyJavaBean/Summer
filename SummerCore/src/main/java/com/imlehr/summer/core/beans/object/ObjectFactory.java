package com.imlehr.summer.core.beans.object;


import com.imlehr.summer.core.annotation.Configuration;
import com.imlehr.summer.core.aop.ProxyManager;
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
            if(beanDefinition.isNeedProxy())
            {
                Object proxy = proxyManager.createProxy(instance, beanDefinition);
                this.proxy = proxy;
                this.origin = instance;
                isProxy = true;

            }
            else
            {
                isProxy = false;
                origin = instance;
            }
        }
        if(isProxy)
        {
            return proxy;
        }
        else
        {
            return origin;
        }
    }

    public Object getOrigin()
    {
        //todo 目前我这个方法会导致有代理的对象实例化两次，也就是构造方法跑两次，不知道源码是不是这样的
        if(instance==null)
        {
            getInstance();
            if(beanDefinition.isNeedProxy())
            {
                Object proxy = proxyManager.createProxy(instance, beanDefinition);
                this.proxy = proxy;
                this.origin = instance;
                isProxy = true;

            }
            else
            {
                isProxy = false;
                origin = instance;
            }
        }
        return origin;
    }

    protected boolean isProxy;

    protected Object proxy;

    protected Object origin;

    protected Object instance;

    protected BeanDefinition beanDefinition;

    public static ProxyManager proxyManager;

    public ObjectFactory(BeanDefinition beanDefinition)
    {
        this.beanDefinition = beanDefinition;
    }

    public static ObjectFactory getFactory(BeanDefinition beanDefinition,ProxyManager proxyManager)
    {
        ObjectFactory.proxyManager = proxyManager;


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
