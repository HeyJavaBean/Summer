package com.imlehr.summer.beans;

import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;

/**
 * @author Lehr
 * @create: 2020-04-27
 */
@Data
@Accessors(chain = true)
public class BeanDefinition {

    //这个其实最好下分到子类去！这个代表的是有@Bean注解的BD对应的方法，调用他获得实例
    private Method method;

    private String beanName;

    private Class beanClass;

    private String scope;

    private boolean lazy;

    //为了使用反射方法来启动方法从而生成bean不得不去记录一下config类
    private String parentName;

    private boolean inited;

    public Boolean notLazy()
    {
        return !lazy;
    }

    public Boolean isSingleton()
    {
        return "singleton".equals(scope);
    }


    public Class[] getParams()
    {
        return null;
    }

    public Object[] getArgs()
    {
        return null;
    }



}
