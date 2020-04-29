package com.imlehr.summer.beans.factory;

import com.imlehr.summer.annotation.*;
import com.imlehr.summer.beans.BeanDefinition;
import com.imlehr.summer.beans.BeanDefinitionHolder;
import com.imlehr.summer.context.BeanDefinitionRegistry;
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

    private final BeanDefinitionRegistry registry;

    public BeanFactory(BeanDefinitionRegistry registry) {
        this.registry = registry;
        //这里还需要注册一些processor但是我懒得写了
    }

    public void registerBeanDefinition(BeanDefinitionHolder bdh) {
        beanDefinitionMap.put(bdh.getBeanName(), bdh.getBeanDefinition());
    }


    public void preInstantiateSingletons() {
        //挨个实例化
        beanDefinitionMap.forEach((name, beanDefinition) ->
        {

            if (beanDefinition.isSingleton() && beanDefinition.notLazy()) {
                getBean(beanDefinition);
            }

        });
    }


    public Map<String, Object> singletonObjects = new HashMap<>();

    /**
     * 给一个BeanDefinition然后实例化后返回Bean
     *
     * @return
     */
    @SneakyThrows
    public Object getBean(BeanDefinition beanDefinition) {
        //这里如果不是配置类直接调用方法获取返回值！！！！
        beanDefinition.setInited(true);
        Object bean = createBean(beanDefinition);
        singletonObjects.put(beanDefinition.getBeanName(), bean);
        return bean;
    }

    @SneakyThrows
    public Object createBean(BeanDefinition beanDefinition) {
        Object entity = singletonObjects.get(beanDefinition.getBeanName());
        if (entity != null) {
            return entity;
        }

        Method beanMethod = beanDefinition.getMethod();
        if(beanMethod!=null)
        {
            if (beanDefinition.getMethod().isAnnotationPresent(Bean.class)) {
                Method method = beanDefinition.getMethod();
                Object configEntity = singletonObjects.get(beanDefinition.getParentName());
                return method.invoke(configEntity);
            }
        }

        //todo 其他操作，比如就是
        //其他不是bean的操作

        Constructor constructor = beanDefinition.getBeanClass().getConstructor(beanDefinition.getParams());
        Object bean = constructor.newInstance(beanDefinition.getArgs());
        singletonObjects.put(beanDefinition.getBeanName(), bean);
        return bean;



    }

    /**
     * 按照名称查找一个Bean
     *
     * @param name
     * @return
     */
    public Object findBean(String name) {
        return singletonObjects.get(name);
    }


    /**
     * 给一个名字获取BeanDefinition
     *
     * @param name
     * @return
     */
    public BeanDefinition getBeanDefinition(String name) {
        return beanDefinitionMap.get(name);
    }

    public void initConfig() {

        beanDefinitionMap.forEach((name, candidate) ->
        {
            if (candidate.getBeanClass().isAnnotationPresent(Configuration.class)) {
                getBean(candidate);
            }
        });

    }


    /**
     * 这里的任务就是从配置类里解析一堆bean然后变成BeanDefinition
     *
     * @param bean
     */
    @SneakyThrows
    private void parserConfig(BeanDefinition bean) {
        Class beanClass = bean.getBeanClass();

        // 遍历所有方法，通过注解找出来有bean的方法
        for (Method method : beanClass.getDeclaredMethods()) {

            if (method.isAnnotationPresent(Bean.class)) {
                // 获取注解
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
                        .setScope(scope).setMethod(method).setParentName(bean.getBeanName());

                beanDefinitionMap.put(name, beanDefinition);

            }
        }
    }

    /**
     * 从配置类里面获取到Bean
     */
    @SneakyThrows
    public void refresh() {

        beanDefinitionMap.forEach((name, candidate) ->
        {
            if (candidate.getBeanClass().isAnnotationPresent(Configuration.class)) {
                parserConfig(candidate);
                scanComponents(candidate);
            }
        });
    }

    public void scanComponents(BeanDefinition config) {
        Class beanClass = config.getBeanClass();
        boolean canScan = beanClass.isAnnotationPresent(ComponentScan.class);
        if (canScan) {
            ComponentScan componentScan = (ComponentScan) beanClass.getAnnotation(ComponentScan.class);
            String[] basePackages = componentScan.value();
            if (basePackages.length < 1) {
                //如果是空的，就默认包是当前的
                basePackages = new String[]{beanClass.getPackageName()};
            }

            this.registry.scan(basePackages);
        }

    }
}
