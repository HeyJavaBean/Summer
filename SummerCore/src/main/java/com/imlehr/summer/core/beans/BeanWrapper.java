package com.imlehr.summer.core.beans;

import com.imlehr.summer.core.annotation.Ordered;
import com.imlehr.summer.core.beans.definition.BeanDefinition;
import com.imlehr.summer.core.beans.object.ObjectFactory;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这个类里主要是有一些用来操作对象的方法，
 * 就相当于封装了一个对象，同时提供了一些方法调试
 * 在源码里这个只是个接口而已
 * 这里是我胡乱设计的
 */
@Data
@Accessors(chain = true)
public class BeanWrapper {

    private String beanName;

    private Object beanEntity;

    private ObjectFactory factory;

    private BeanDefinition beanDefinition;

    /**
     * 准备的属性
     */
    private Map<Field,Object> fieldMap = new HashMap<>();



    public void flush()
    {
        fieldMap.forEach((f,o)->
        {
            try
            {
                f.setAccessible(true);
                f.set(beanEntity,o);
            }catch (Exception e)
            {
                System.out.println("fuck you");
            }
        });
    }

}
