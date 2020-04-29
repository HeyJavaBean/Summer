package com.imlehr.summer.context.annotation;

import com.imlehr.summer.annotation.Component;
import com.imlehr.summer.annotation.Controller;
import com.imlehr.summer.annotation.Repository;
import com.imlehr.summer.annotation.Service;
import com.imlehr.summer.beans.BeanDefinition;
import com.imlehr.summer.beans.BeanDefinitionHolder;
import com.imlehr.summer.context.BeanDefinitionRegistry;
import lombok.SneakyThrows;

import java.io.File;
import java.security.Provider;

/**
 * @author Lehr
 * @create: 2020-04-29
 * 用来做类扫描的，暂时按照自己的想法随心所欲的写的
 */
public class ClassPathBeanDefinitionScanner {

    private final BeanDefinitionRegistry registry;

    private final String classPath = System.getProperty("user.dir")+ File.separator +"build"+File.separator + "classes/java/main";

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry)
    {
        this.registry = registry;
        //这里还需要注册一些processor但是我懒得写了
    }

    public void scan(String... basePackages)
    {
        for (String basePackage : basePackages) {
            doScan(basePackage);
        }
    }

    @SneakyThrows
    private void doScan(String basePackage)
    {
        //todo:这里通过ClassPath的方法去扫描加载这些类？目前就只能这个样子咯？反正类加载器先用默认的

        String basePackageUrl = classPath  +File.separator+ basePackage.replace(".", File.separator);

        File f = new File(basePackageUrl);

        recrusiveFile(f);

    }

    private void recrusiveFile(File f) {
        for (File file : f.listFiles()) {
            if (file.isDirectory()) {
                recrusiveFile(file);
            } else {
                scanFile(file);
            }
        }
    }

    @SneakyThrows
    private void scanFile(File file)
    {
        if(file.getName().endsWith(".class"))
        {

            String absolutePath = file.getAbsolutePath();
            String substring = absolutePath.substring(classPath.length()+1, absolutePath.length() -6);
            String packageName = substring.replace(File.separator, ".");
            Class<?> target = Class.forName(packageName);
            if(hasTag(target))
            {
                //开始把他变成beanDefinition
                //fixme 暂时不支持写名字，等我把service什么都改成Component注解之后再说...


                BeanDefinition bean = new BeanDefinition().setBeanClass(target).setBeanName(target.getName());

                //感觉bean解析太分散了....
                bean.setLazy(false).setScope("singleton");
                BeanDefinitionHolder bdh = new BeanDefinitionHolder().setBeanName(target.getName()).setBeanDefinition(bean);
                //下面是我简写的
                registry.getBeanFactory().registerBeanDefinition(bdh);

            }
        }
    }

    private boolean hasTag(Class clazz)
    {
        return clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Repository.class) || clazz.isAnnotationPresent(Controller.class);
    }



}
