package com.imlehr.summer.context.annotation;

import com.imlehr.summer.beans.BeanDefinition;
import com.imlehr.summer.context.BeanDefinitionRegistry;

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
        //这里还需要注册一些processor但是我懒得写了
    }

    /**
     * 注册beans
     * 这里我就简写了，接口就不一样了
     * @param component
     */
    public void doRegisterBean(Class component)
    {
        //把他抽成bean，然后我把默认的方法配置全部写到里面去了
        BeanDefinition bean = new BeanDefinition().setBeanClass(component).setBeanName(component.getName());

        //下面是我简写的
        registry.getBeanFactory().registerBeanDefinition(bean);
    }

}
