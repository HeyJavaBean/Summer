package com.imlehr.summer.beans.factory;

import com.imlehr.summer.annotation.Bean;
import com.imlehr.summer.beans.BeanDefinition;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lehr
 * @create: 2020-04-27
 */
public class BeanFactory {

    /**
     * 原版也是用的一个concurrentHashmap
     */
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private String configName;

    public void registerBeanDefinition(BeanDefinition bean) {
        configName = bean.getBeanName();
        beanDefinitionMap.put(bean.getBeanName(), bean);
    }


    public void preInstantiateSingletons()
    {
        //挨个实例化
        beanDefinitionMap.forEach((name,beanDefinition)->
        {
            if(beanDefinition.isSingleton() && beanDefinition.notLazy())
            {
                Object bean = getBean(beanDefinition);
                singletonObjects.put(beanDefinition.getBeanName(),bean);
            }

        });
    }


    public Map<String ,Object> singletonObjects = new HashMap<>();

    @SneakyThrows
    public Object getBean(BeanDefinition bean)
    {
        //这里如果不是配置类直接调用方法获取返回值！！！！
        return bean.getMethod().invoke(singletonObjects.get(configName));
    }

    public Object findBean(String name)
    {
        return singletonObjects.get(name);
    }


    public BeanDefinition getBeanDefinition(String name)
    {
        return beanDefinitionMap.get(name);
    }

    public void initLazy(BeanDefinition bean)
    {
        singletonObjects.put(bean.getBeanName(),getBean(bean));
        bean.setLazy(false);
    }

    @SneakyThrows
    public void refresh() {
        BeanDefinition bean = getBeanDefinition(configName);

        Class beanClass = bean.getBeanClass();

        //先整好配置类实例化
        Constructor constructor = bean.getBeanClass().getConstructor(bean.getParams());
        Object o = constructor.newInstance(bean.getArgs());
        singletonObjects.put(bean.getBeanName(),o);

        // 遍历所有方法，通过注解找出来有bean的方法
        for (Method method : beanClass.getDeclaredMethods()) {

            if (method.isAnnotationPresent(Bean.class)) {
                // 获取注解
                Bean annotation = method.getAnnotation(Bean.class);

                String name = annotation.name();

                if(name==null||name.length()<1)
                {
                    name = method.getName();
                }

                boolean lazy = annotation.lazy();

                String scope = annotation.scope();

                Class<?> returnType = method.getReturnType();

                //todo 参数怎么办？

                BeanDefinition beanDefinition = new BeanDefinition().setBeanClass(returnType).setBeanName(name).setLazy(lazy).setScope(scope).setMethod(method);

                beanDefinitionMap.put(name,beanDefinition);

            }
        }
    }
}
