package com.imlehr.summer.core.context.component;

import com.beust.jcommander.internal.Lists;
import com.imlehr.summer.core.annotation.scan.Component;
import com.imlehr.summer.core.annotation.scan.Controller;
import com.imlehr.summer.core.annotation.scan.Repository;
import com.imlehr.summer.core.annotation.scan.Service;
import com.imlehr.summer.core.beans.definition.BeanDefinitionRegistry;
import com.imlehr.summer.core.utils.LoadingUtils;
import lombok.SneakyThrows;

import java.io.File;
import java.util.List;

/**
 * @author Lehr
 * @create: 2020-04-29
 * 用来做类扫描的，暂时按照自己的想法随心所欲的写的
 */
public class ClassPathBeanDefinitionScanner {

    private final BeanDefinitionRegistry registry;

    private final String classPath = System.getProperty("user.dir") + File.separator + "build" + File.separator + "classes/java/main";

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
        //这里还需要注册一些processor但是我懒得写了
    }

    /**
     * 扫描特定路径下的包，解析相应的BeanDefinition
     * @param basePackages
     */
    public void scan(String... basePackages) {
        for (String basePackage : basePackages) {
            //具体动作
            doScan(basePackage);
        }
    }

    /**
     * 具体扫描某个路径下的包，解析相应的BeanDefinition
     * 这个是我自己设计实现的，没太看Spring源码是怎么做的
     * @param basePackage
     */
    @SneakyThrows
    private void doScan(String basePackage) {

        //递归获取当前包下的所有类
        List<Class> packageClasses = LoadingUtils.recrusivePackage(basePackage);
        List<Class> componentClasses = Lists.newArrayList();
        packageClasses.forEach(c->
        {
            //提取有特定标签的
            if(hasTag(c))
            {
                componentClasses.add(c);
            }
        });

        //然后再拿去给register注册
        Class[] classes = new Class[componentClasses.size()];
        componentClasses.toArray(classes);
        registry.register(classes);


    }

    /**
     * 递归找文件，然后用类加载器解析而已
     * @param f
     * @param componentClasses
     */
    @SneakyThrows
    private void recrusiveScan(File f, List componentClasses) {
        for (File file : f.listFiles()) {
            if (file.isDirectory()) {
                recrusiveScan(file, componentClasses);
            } else {
                if (file.getName().endsWith(".class")) {
                    //fixme 这段获取类名的写法，强烈需要改进
                    String absolutePath = file.getAbsolutePath();
                    String substring = absolutePath.substring(classPath.length() + 1, absolutePath.length() - 6);
                    String packageName = substring.replace(File.separator, ".");
                    Class<?> target = Class.forName(packageName);
                    //找到有特定注解的才能拿去
                    if (hasTag(target)) {
                        componentClasses.add(target);
                    }
                }
            }
        }
    }


    /**
     * 检查自动注册的目标是否具有标签
     * @param clazz
     * @return
     */
    private boolean hasTag(Class clazz) {
        return clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Repository.class) || clazz.isAnnotationPresent(Controller.class);
    }


}
