package com.imlehr.summer.core.context;

import com.imlehr.summer.core.beans.definition.BeanDefinition;
import com.imlehr.summer.core.beans.definition.BeanDefinitionRegistry;
import com.imlehr.summer.core.beans.factory.BeanFactory;
import com.imlehr.summer.core.context.component.AnnotatedBeanDefinitionReader;
;
import com.imlehr.summer.core.context.component.ClassPathBeanDefinitionScanner;
import com.imlehr.summer.core.utils.Assert;

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
     * 构造方法，传入一个config，然后就直接生成相应的内容了
     */
    public AnnotationConfigApplicationContext(Class... componentClasses)
    {
        //初始化组件
        this();
        //注册配置信息，执行后，BeanFactory里只有注册类的BeanDefinition
        register(componentClasses);
        //刷新，启动容器！
        refresh();
    }

    /**
     * 让reader去通过读取Class的方式注册BeanDefinition
     */
    @Override
    public void register(Class... componentClasses) {
        Assert.notEmpty(componentClasses, "At least one component class must be specified");
        //用reader来读取BeanDefinition
        reader.register(componentClasses);
    }

    /**
     * 暂时不考虑线程安全和异常情况
     * 不支持扩展功能
     * 在加载好了之前的配置之后
     * 驱动整个IoC容器启动
     * 这里是超级简单版的迷你实现
     */
    public void refresh() {

        //先对BeanDefinition进行排序，就是之前注册的Order信息
        //todo: 不过我记不得这个步骤是我自己设计的还是源码也这样的，按理说后面还需要排一次顺序
        beanFactory.sortBeanDefinition();

        //出于设计需求，这里就先把config给实例化了，因为我后面都需要用到这个config实例
        //todo: 这里可能会遇到config的循环依赖问题，先跳过这个问题，不考虑这个
        beanFactory.preInit();

        //这里是用来把配置类里的所有内容提取出来用，包括扫描的东西，全部变成BeanDefinition
        beanFactory.refresh();

        //再次排序，现在这里面就有我们所有想要的类的BeanDefinition而且排好顺序了
        beanFactory.sortBeanDefinition();

        //这里现在变成了配置扫描
        beanFactory.getProxy();

        //彻底把所有的bean definition给bean化了
        // Instantiate all remaining (non-lazy-init) singletons.
        //对应的是源码的finishBeanFactoryInitialization这一步
        beanFactory.preInstantiateSingletons();

    }


    /**
     * 初始化各个组件，然后准备一个我自己的BeanFactory
     */
    public AnnotationConfigApplicationContext() {
        // 给定目标，读入组件
        reader = new AnnotatedBeanDefinitionReader(this);
        // 对目标包进行扫描用的
        scanner = new ClassPathBeanDefinitionScanner(this);
        //Bean工厂初始化  在源码里这个是在父级实现的
        beanFactory = new BeanFactory(this);
    }


    /**
     * Create a new AnnotationConfigApplicationContext, scanning for components
     * in the given packages, registering bean definitions for those components,
     * and automatically refreshing the context.
     * @param basePackages the packages to scan for component classes
     */
    public AnnotationConfigApplicationContext(String... basePackages) {
        this();
        scan(basePackages);
        refresh();
    }

    @Override
    public BeanFactory getBeanFactory()
    {
        return beanFactory;
    }

    /**
     * 使用扫描器进行扫描，把对应路径下的类变成BeanDefinition
     * 这里我就先自己设计了，和源码的可能意思不太一样
     * @param basePackages
     */
    @Override
    public void scan(String... basePackages) {
        Assert.notEmpty(basePackages, "At least one base package must be specified");
        scanner.scan(basePackages);
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


    //todo 源码部分还有一个registerBean，好像和这个不太一样？？
    /**
     * 把在Config里以方法形式配置的Bean给注册为BeanDefinition
     * @param beans
     * @param configBean
     */
    @Override
    public void registBean(List<Method> beans,Object configBean) {
        //这个任务还是reader来做，读取内容并生成BeanDefinition
        reader.registBeanAnotation(beans,configBean);
    }





}
