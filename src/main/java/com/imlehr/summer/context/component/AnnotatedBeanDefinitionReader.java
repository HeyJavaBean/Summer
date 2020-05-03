package com.imlehr.summer.context.component;

import com.imlehr.summer.annotation.Autowired;
import com.imlehr.summer.annotation.Bean;
import com.imlehr.summer.annotation.Lazy;
import com.imlehr.summer.annotation.Scope;
import com.imlehr.summer.beans.definition.BeanDefinition;
import com.imlehr.summer.beans.definition.BeanDefinitionHolder;
import com.imlehr.summer.beans.definition.BeanDefinitionRegistry;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lehr
 * @create: 2020-04-27
 *  AnnotatedBeanDefinitionReader是一个读取注解的Bean读取器
 */
public class AnnotatedBeanDefinitionReader {

    private BeanDefinitionRegistry registry;

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry)
    {
        this.registry = registry;
    }

    public void register(Class... componentClasses)
    {
        for (Class<?> componentClass : componentClasses) {
            doRegisterBean(componentClass);
        }
    }

    /**
     * 注册beans
     */
    private void doRegisterBean(Class component)
    {
        //把他抽成bean，然后我把默认的方法配置全部写到里面去了
        BeanDefinition bean = new BeanDefinition()
                .setBeanClass(component)
                .setBeanName(component.getName())
                .setLazy(false)
                .setInited(false)
                .setScope("singleton");

        List<Field> autowireList = new ArrayList<>();

        //拿到autowired的类
        for (Field field : component.getDeclaredFields()) {
            if(field.isAnnotationPresent(Autowired.class))
            {
                autowireList.add(field);
            }
        }

        bean.setAutowireList(autowireList);

        BeanDefinitionHolder bdh = new BeanDefinitionHolder().setBeanName(component.getName()).setBeanDefinition(bean);
        //下面是我简写的
        registry.getBeanFactory().registerBeanDefinition(bdh);
    }

    /**
     * 这个是我专门为了处理有@bean的情况处理的解析bean definition的情况
     */
    public void registBeanAnotation(List<Method> beans,Object configBean)
    {
        beans.forEach(bean->
        {
            doRegistBeanAnnotation(bean,configBean);
        });
    }

    /**
     * 这个其实就是模仿doProcessConfigurationClass来写的
     * 如果想模拟官方的实现，则可以去看看SourceClass和ConfigurationClass
     * 反正我先按照自己的想法去写了
     * @param method
     */
    private void doRegistBeanAnnotation(Method method,Object configBean)
    {
        // 获取注解  但是问题是，不得不去和配置类关联起来
        Bean annotation = method.getAnnotation(Bean.class);

        String name = annotation.name();

        if (name == null || name.length() < 1) {
            name = method.getName();
        }

        boolean lazy = method.isAnnotationPresent(Lazy.class);

        String scope = "singleton";

        if (method.isAnnotationPresent(Scope.class)) {
            Scope scopeTag = method.getAnnotation(Scope.class);
            scope = scopeTag.value();
        }

        Class<?> returnType = method.getReturnType();

        BeanDefinition beanDefinition = new BeanDefinition().setBeanClass(returnType)
                .setBeanName(name).setLazy(lazy)
                .setScope(scope).setMethod(method).setConfigBean(configBean);

        BeanDefinitionHolder bdh = new BeanDefinitionHolder().setBeanName(name).setBeanDefinition(beanDefinition);
        //下面是我简写的
        registry.getBeanFactory().registerBeanDefinition(bdh);
    }

}
