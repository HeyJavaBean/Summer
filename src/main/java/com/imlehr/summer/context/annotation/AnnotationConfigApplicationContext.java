package com.imlehr.summer.context.annotation;

import com.imlehr.summer.beans.BeanDefinition;
import com.imlehr.summer.beans.factory.BeanFactory;
import com.imlehr.summer.context.ApplicationContext;
import com.imlehr.summer.context.BeanDefinitionRegistry;

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
        // AnnotatedBeanDefinitionReader是一个读取注解的Bean读取器,现在就是准备组件而已
        reader = new AnnotatedBeanDefinitionReader(this);
        //todo 初始化设置
        beanFactory = new BeanFactory(this);
        //这个是用来扫描默认标签用的东西
        scanner = new ClassPathBeanDefinitionScanner(this);
    }

    /**
     * 让reader去注册他
     */
    public void register(Class... componentClasses) {
        //懒得做NPE处理了 这里就是把注册到的那个放入到beanFactory里
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


        //先整好配置类实例化
        beanFactory.initConfig();

        //这里是用来把配置类里的所有内容提取出来用，包括扫描的东西
        beanFactory.refresh();

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
            return beanFactory.createBean(beanDefinition);
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



}
