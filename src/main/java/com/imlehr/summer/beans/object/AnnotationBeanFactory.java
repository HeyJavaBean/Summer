package com.imlehr.summer.beans.object;

import com.imlehr.summer.annotation.Bean;
import com.imlehr.summer.beans.definition.BeanDefinition;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

/**
 * @author Lehr
 * @create: 2020-04-30
 * 处理bean的源码在configurationClassParser里的doxxx方法
 */
public class AnnotationBeanFactory extends ObjectFactory {


    public AnnotationBeanFactory(BeanDefinition beanDefinition)
    {
        super(beanDefinition);
    }


    @Override
    @SneakyThrows
    public void getInstance()
    {
        //检查是不是bean注解情况方法
        Method beanMethod = beanDefinition.getMethod();
        if (beanMethod != null) {
            if (beanDefinition.getMethod().isAnnotationPresent(Bean.class)) {
                Method method = beanDefinition.getMethod();
                instance =  method.invoke(beanDefinition.getConfigBean());
            }
        }
    }


}
