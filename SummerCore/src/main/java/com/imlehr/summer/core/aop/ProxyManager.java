package com.imlehr.summer.core.aop;

import com.imlehr.summer.core.annotation.aspect.*;
import com.imlehr.summer.core.aop.beans.AopConfig;
import com.imlehr.summer.core.beans.BeanWrapper;
import com.imlehr.summer.core.beans.definition.BeanDefinition;
import com.imlehr.summer.core.beans.factory.BeanFactory;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Lehr
 * @create: 2020-09-08
 * 一个自己随便设计的类，用来处理代理和AOP相关的代码
 */
public class ProxyManager {

    private BeanFactory beanFactory;

    public ProxyManager(BeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }

    /**
     * 读取某个Aspect切面配置文件，然后开始给BeanDefinition打标记而不是先实例化对象
     * 这里采用的是CGLib的方法
     * @param beanDefinition
     */
    public void doProxy(BeanDefinition beanDefinition)
    {
        //先准备一个对象，用来记录Aop设定
        AopConfig aopConfig = new AopConfig();

        //绑定上这一个具体的Aspect设定
        //这里在GetBean的时候就已经先实例化这个配置文件了（主要是语因为我后面的设计思路，todo :诶等下，似乎不用？）
        aopConfig.setEntity(beanFactory.getBean(beanDefinition));

        //扫描这个Aspect，寻找切点
        for (Method method : beanDefinition.getBeanClass().getDeclaredMethods()) {

            //todo 所以这里只能处理这种残疾的写法,后面的方法都只能套在pointcut上面
            //todo 艹我似乎又忘记了那个切面语法了.....
            //todo 其实这里用责任链会比较好

            //找到切点，确定切点作用的类or方法范围
            if(method.isAnnotationPresent(Pointcut.class))
            {
                Pointcut p = method.getAnnotation(Pointcut.class);
                String el = p.value();
                //给入el，切出class，这里是写的一个有限自动机实现ing
                List<Class> classes = AspectJUtils.parseAspectEL(el);
                //设置好目标对象？？ todo
                aopConfig.setAopClassess(classes);
                continue;
            }
            //设置切点周围的环绕，todo 我有点记不得规则了艹，而且我觉得下面这段垃圾代码可以优化一下
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
            if(method.isAnnotationPresent(Around.class))
            {
                aopConfig.setAroundMethod(method);
                continue;
            }
            if(method.isAnnotationPresent(AfterReturning.class))
            {
                AfterReturning tag = method.getAnnotation(AfterReturning.class);
                aopConfig.setResultName(tag.returning());
                aopConfig.setAfterReturningMethod(method);
                continue;
            }
            if(method.isAnnotationPresent(AfterThrowing.class))
            {
                AfterThrowing tag = method.getAnnotation(AfterThrowing.class);
                aopConfig.setExceptionName(tag.throwing());
                aopConfig.setAfterThrowingMethod(method);
                continue;
            }



        }

        aopConfig.getAopClassess().forEach(cls->{
            BeanDefinition bd = beanFactory.getBeanDefinition(cls.getName());
            bd.setNeedProxy(true);
            bd.setAopConfig(aopConfig);
        });
        
    }


    /**
     * 这个方法则变成了直接实例化对象了
     */
    public Object createProxy(Object bean,BeanDefinition beanDefinition)
    {
        //TODO 暂时在处理名字上有困难，默认用类名，查找还需要改进

        //TODO 循环依赖的问题还没有解决，导致会拿到原生对象
        //拿到Bean（如果没有的话就自动创建）

        AopConfig aopConfig = beanDefinition.getAopConfig();

        //CGLib开始搞
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bean.getClass());
        enhancer.setCallback(new AopMethodIntercreptor(bean,aopConfig));

        //获取到代理对象
        Object proxy = enhancer.create();
        //todo 由于 Cglib 本身的设计，无法实现在 Proxy 外面再包装一层 Proxy
        // 所以暂时没法实现多切面运行顺序
        // 通常会报如下错误：
        // Duplicate method name "newInstance" with signature
        // 具体 ： https://www.jianshu.com/p/9ba77d8f200b
        // 个人暂时还没有能力解决这个问题

        return proxy;

    }




}
