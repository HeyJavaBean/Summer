package com.imlehr.summer.core.context.component;

import com.imlehr.summer.core.annotation.Bean;
import com.imlehr.summer.core.beans.definition.BeanDefinition;
import com.imlehr.summer.core.beans.definition.BeanDefinitionHolder;
import com.imlehr.summer.core.beans.definition.BeanDefinitionRegistry;
import com.imlehr.summer.core.annotation.*;


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

    /**
     * 这个注册器其实就是IoC Context本身
     * 这个思路和Tomcat之间容器的关系一样的
     */
    private BeanDefinitionRegistry registry;

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry)
    {
        this.registry = registry;
    }

    /**
     * 拿到类信息然后进行注册用的，可以传入多个config类来配置
     * 也可以是其他你想注册的Class(自动扫描的时候会调用)
     * @param componentClasses
     */
    public void register(Class... componentClasses)
    {
        for (Class<?> componentClass : componentClasses) {
            doRegisterBean(componentClass);
        }
    }

    /**
     * 通过Class注册为BeanDefinition
     * (除了这个还有另外一种途径，就是通过方法去注册)
     * todo 源码的registerBean和这里不太一样，又抽象了一次，那个supplier接口之类的没看懂
     * 总之这里的工作就是传入一个类，然后抽取出元信息，源码的方法检测各种东西 比较复杂 所以没管了
     */
    private void doRegisterBean(Class component)
    {
        //把配置文件抽象为BeanDefinition，然后填写默认内容
        BeanDefinition bean = new BeanDefinition()
                //设置真正的类
                .setBeanClass(component)
                //设置BeanName，默认为类的名字
                .setBeanName(component.getName())
                //不是懒加载
                .setLazy(false)
                //是否被实例化了？默认是否
                .setInited(false)
                //默认是单例模式
                .setScope("singleton");

        //如果有加载顺序指定的Order标签，则读取并获取
        if(component.isAnnotationPresent(Order.class))
        {
            Order order = (Order)component.getAnnotation(Order.class);
            bean.setOrder(order.value());
        }

        //检查这个类的成员，看是否有需要自动注入的？
        List<Field> autowireList = new ArrayList<>();

        //扫描每一个成员，查看是否有autowire标签
        for (Field field : component.getDeclaredFields()) {
            if(field.isAnnotationPresent(Autowired.class))
            {
                autowireList.add(field);
            }
        }

        //在BeanDefinition里填写好要autowire的类的list
        bean.setAutowireList(autowireList);

        //以类名->BeanDefinition的方法，封装到bdh里
        //todo: 这里我忽然想不起在源码里他这样做的意义了....
        BeanDefinitionHolder bdh = new BeanDefinitionHolder().setBeanName(component.getName()).setBeanDefinition(bean);
        //交给registry注册器（其实就是关联回Context容器）去注册
        //todo: 忽然想不起这种设计模式叫什么了。反正tomcat里也有这个操作
        //源码的话是用了个工具方法写的，反正大概就也是这个意思
        registry.getBeanFactory().registerBeanDefinition(bdh);
    }

    /**
     * 用于把Config里通过Bean标签配置的方法的类解析注册的
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
        // 获取注解
        // todo：但是问题是，在我的实现里不得不去和配置类关联起来，也就是configBean
        Bean annotation = method.getAnnotation(Bean.class);

        String name = annotation.name();

        //确定名字
        if (name == null || name.length() < 1) {
            name = method.getName();
        }

        //是否懒加载
        boolean lazy = method.isAnnotationPresent(Lazy.class);

        //确定是singleton还是什么，Scope
        String scope = "singleton";

        if (method.isAnnotationPresent(Scope.class)) {
            Scope scopeTag = method.getAnnotation(Scope.class);
            scope = scopeTag.value();
        }

        Class<?> returnType = method.getReturnType();

        //设置好这个bean
        BeanDefinition beanDefinition = new BeanDefinition().setBeanClass(returnType)
                .setBeanName(name).setLazy(lazy)
                .setScope(scope).setMethod(method).setConfigBean(configBean);

        //设置顺序
        if(method.isAnnotationPresent(Order.class))
        {
            Order order = method.getAnnotation(Order.class);
            beanDefinition.setOrder(order.value());
        }

        BeanDefinitionHolder bdh = new BeanDefinitionHolder().setBeanName(name).setBeanDefinition(beanDefinition);
        //加入BeanFactory
        registry.getBeanFactory().registerBeanDefinition(bdh);
    }

}
