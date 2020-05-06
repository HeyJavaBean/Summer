package com.imlehr.summer.beans.factory;

import com.imlehr.summer.annotation.*;
import com.imlehr.summer.annotation.aspect.After;
import com.imlehr.summer.annotation.aspect.Aspect;
import com.imlehr.summer.annotation.aspect.Before;
import com.imlehr.summer.annotation.aspect.Pointcut;
import com.imlehr.summer.beans.AopConfig;
import com.imlehr.summer.beans.definition.BeanDefinition;
import com.imlehr.summer.beans.definition.BeanDefinitionHolder;
import com.imlehr.summer.beans.object.ObjectFactory;
import com.imlehr.summer.beans.definition.BeanDefinitionRegistry;
import com.imlehr.summer.context.AopHandler;
import com.imlehr.summer.test.scanner.component.TestInterface;
import com.imlehr.summer.utils.AspectJUtils;
import lombok.SneakyThrows;

import java.lang.reflect.*;
import java.util.*;
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


    /**
     * 一级缓存，用于存放完全初始化好的 bean，从该缓存中取出的 bean 可以直接使用
     */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    /**
     * 二级缓存 存放原始的 bean 对象（尚未填充属性），用于解决循环依赖
     */
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    /**
     * 三级缓存 存放 bean 工厂对象，用于解决循环依赖
     */
    private final Map<String, ObjectFactory> singletonFactories = new HashMap<>(16);


    /**
     * new完了但是还没有填写信息的情况
     */
    private final Set<String> singletonsCurrentlyInCreation =
            Collections.newSetFromMap(new ConcurrentHashMap<>(16));


    public boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }


    /**
     * 给一个BeanDefinition然后实例化后返回Bean
     * 这里就是重点了！！！
     *
     * @return
     */
    @SneakyThrows
    public Object getBean(BeanDefinition beanDefinition) {

        //这里如果不是配置类直接调用方法获取返回值！！！！
        String beanName = beanDefinition.getBeanName();
        //首先从缓存池里尝试获取，这里就会走一级二级三级
        Object sharedInstance = getSingleton(beanName);

        //如果说是获取到了（但是有可能是个半成品）
        if (sharedInstance != null) {
            //使用某个方法完善实例化?

        } else {
            //开始实例化
            singletonsCurrentlyInCreation.add(beanName);
            sharedInstance = doCreateBean(beanDefinition);
            singletonObjects.put(beanName,sharedInstance);
            singletonsCurrentlyInCreation.remove(beanName);
        }

        return sharedInstance;
    }


    private Object doCreateBean(BeanDefinition beanDefinition) {
        //new出对象
        ObjectFactory objectFactory = ObjectFactory.getFactory(beanDefinition);
        //放入到三级缓存里去
        singletonFactories.put(beanDefinition.getBeanName(), objectFactory);

        //创建对象
        Object bean = objectFactory.getObject();
        //注入属性
        List<Field> autowireList = beanDefinition.getAutowireList();
        if(autowireList!=null && !autowireList.isEmpty())
        {
            autowireList.forEach(ab->
            {
                beanDefinitionMap.values().forEach(bd->
                {
                    if(bd.getBeanClass().equals(ab.getType()))
                    {
                        Object field = getBean(bd);
                        try {
                            ab.setAccessible(true);
                            ab.set(bean,field);
                        } catch (IllegalAccessException e) {
                            System.out.println("属性注入出错了我靠");
                            e.printStackTrace();
                        }
                    }
                });
            });
        }

        return bean;
    }


    public Object createProto(BeanDefinition beanDefinition)
    {
        ObjectFactory objectFactory = ObjectFactory.getFactory(beanDefinition);

        Object bean = objectFactory.getObject();
        List<Field> autowireList = beanDefinition.getAutowireList();
        if(autowireList!=null && !autowireList.isEmpty())
        {
            autowireList.forEach(ab->
            {
                beanDefinitionMap.values().forEach(bd->
                {
                    if(bd.getBeanClass().equals(ab.getType()))
                    {
                        Object field = getBean(bd);
                        try {
                            ab.setAccessible(true);
                            ab.set(bean,field);
                        } catch (IllegalAccessException e) {
                            System.out.println("属性注入出错了我靠");
                            e.printStackTrace();
                        }
                    }
                });
            });
        }


        return bean;

    }






    private Object getSingleton(String beanName) {
        //先从一级缓存池里尝试获取
        Object singletonObject = this.singletonObjects.get(beanName);
        //如果是空的 或者说是正在创建的状态
        if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {

            //从二级缓存early里获取提前曝光的内容
            singletonObject = this.earlySingletonObjects.get(beanName);
            if (singletonObject == null) {
                //如果还是空的，就从 三级缓存里去获取，然后获取到了之后把他放到二级缓存里去
                ObjectFactory singletonFactory = this.singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    this.earlySingletonObjects.put(beanName, singletonObject);
                    this.singletonFactories.remove(beanName);
                }

            }
        }
        return singletonObject;
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

    /**
     * 初始化之前已经加载了的东西
     */
    public void preInit() {
        beanDefinitionMap.values().forEach(bd->getBean(bd));
    }


    /**
     * 这里的任务就是从配置类里解析一堆bean然后变成BeanDefinition
     *
     * @param configBean
     */
    @SneakyThrows
    private void parserConfig(BeanDefinition configBean) {
        Class beanClass = configBean.getBeanClass();

        List<Method> beans = new ArrayList<>();
        // 遍历所有方法，通过注解找出来有bean的方法
        for (Method method : beanClass.getDeclaredMethods()) {

            if (method.isAnnotationPresent(Bean.class)) {
                beans.add(method);
            }
        }

        registry.registBean(beans,getBean(configBean));

    }

    /**
     * 从配置类里面获取到Bean
     * 这里是完善所有bean的注册内容
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



    public void getProxy()
    {
        beanDefinitionMap.values().forEach(bd->
        {
            if(bd.getBeanClass().isAnnotationPresent(Aspect.class))
            {
                doProxy(bd);
            }
        });
    }

    private void doProxy(BeanDefinition beanDefinition)
    {
        AopConfig aopConfig = new AopConfig();

        aopConfig.setEntity(getBean(beanDefinition));

        for (Method method : beanDefinition.getBeanClass().getDeclaredMethods()) {

            //todo 所以这里只能处理这种残疾的写法,后面的方法都只能套在pointcut上面

            if(method.isAnnotationPresent(Pointcut.class))
            {
                Pointcut p = method.getAnnotation(Pointcut.class);
                String el = p.value();
                List<Class> classes = AspectJUtils.parseAspectEL(el);
                aopConfig.setAopClassess(classes);
                continue;
            }
            if(method.isAnnotationPresent(Before.class))
            {
                aopConfig.setBeforeMethod(method);
                continue;
            }
            if(method.isAnnotationPresent(After.class))
            {
                aopConfig.setAfterMethod(method);
                continue;
            }


        }

        createProxy(aopConfig);

    }

    private void createProxy(AopConfig aopConfig)
    {
        //TODO 暂时在处理名字上有困难，默认用类名，查找还需要改进
        aopConfig.getAopClassess().forEach(cls->{
            Object bean = getBean(beanDefinitionMap.get(cls.getName()));
            Object proxy = doCreateProxyBean(bean, aopConfig);

            //todo 目前是使用JDK动态代理，所以还是没能解决非要让aspect对象有接口的问题

            singletonObjects.put(cls.getName(),proxy);
        });

    }

    private Object doCreateProxyBean(Object target, AopConfig aopConfig)
    {

        Object o = Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
                new AopHandler(target,aopConfig));
        return o;
    }

}
