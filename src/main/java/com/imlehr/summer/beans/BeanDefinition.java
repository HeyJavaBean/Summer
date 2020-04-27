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

    //这个其实最好下分到子类去！
    private Method method;

    private String beanName;

    private Class beanClass;

    private String scope;

    private boolean lazy;

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
