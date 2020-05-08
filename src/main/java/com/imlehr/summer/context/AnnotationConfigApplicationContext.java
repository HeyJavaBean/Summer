package com.imlehr.summer.context;

import com.imlehr.summer.beans.definition.BeanDefinition;
import com.imlehr.summer.beans.factory.BeanFactory;
import com.imlehr.summer.context.ApplicationContext;
import com.imlehr.summer.beans.definition.BeanDefinitionRegistry;
import com.imlehr.summer.context.component.AnnotatedBeanDefinitionReader;
import com.imlehr.summer.context.component.ClassPathBeanDefinitionScanner;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Lehr
 * @create: 2020-04-27
 * 实现了两个接口，目前设计比较简单而已
 */
public class AnnotationConfigApplicationContext implements ApplicationContext, BeanDefinitionRegistry {

    private final AnnotatedBeanDefinitionReader reader;

    private final ClassPathBeanDefinitionScanner scanner;

    private BeanFactory beanFactory;




    /**
     * 准备reader，然后我自己设计的这里是准备BeanFactory
     */
    public AnnotationConfigApplicationContext() {
        // 给定目标，读入组件
        reader = new AnnotatedBeanDefinitionReader(this);
        // 对目标包进行扫描用的
        scanner = new ClassPathBeanDefinitionScanner(this);
        //todo 初始化设置
        beanFactory = new BeanFactory(this);
    }

    /**
     * 让reader去注册他
     */
    @Override
    public void register(Class... componentClasses) {
        this.reader.register(componentClasses);
    }

    @Override
    public BeanFactory getBeanFactory()
    {
        return beanFactory;
    }

    /**
     * 使用扫描器进行扫描，这里我就先自己设计了
     * @param basePackages
     */
    @Override
    public void scan(String... basePackages) {
        scanner.scan(basePackages);
    }

    /**
     * 暂时不考虑线程安全和异常情况
     * 不支持扩展功能
     */
    public void refresh() {


        beanFactory.sortBeanDefinition();
        //出于设计需求，这里就先把config给实例化了，这里可能会遇到config的循环依赖问题，先跳过这个问题咱们
        beanFactory.preInit();

        //这里是用来把配置类里的所有内容提取出来用，包括扫描的东西
        beanFactory.refresh();

        beanFactory.sortBeanDefinition();
        //然后再把aop的配置给弄了，提前生成好代理对象
        beanFactory.getProxy();

        //彻底把所有的bean definition给bean化了
        // Instantiate all remaining (non-lazy-init) singletons.
        beanFactory.preInstantiateSingletons();

    }

    @Override
    public Object getBean(String name)
    {
        return doGetBean(name);
    }

    private Object doGetBean(String name)
    {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
        if(beanDefinition.isSingleton())
        {
            if(beanDefinition.isLazy() && !beanDefinition.isInited())
            {
                beanFactory.getBean(beanDefinition);
            }
            return beanFactory.findBean(name);
        }
        else
        {
            return beanFactory.createProto(beanDefinition);
        }
    }



    /**
     * 这里目前是只支持传入一个类，以后再来扩展
     * component
     */
    public AnnotationConfigApplicationContext(Class... componentClasses)
    {
        this();
        register(componentClasses);
        refresh();
    }

    @Override
    public void registBean(List<Method> beans,Object configBean) {
        reader.registBeanAnotation(beans,configBean);
    }
}
