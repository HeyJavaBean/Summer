package com.imlehr.summer.context.component;

import com.imlehr.summer.annotation.Component;
import com.imlehr.summer.annotation.Controller;
import com.imlehr.summer.annotation.Repository;
import com.imlehr.summer.annotation.Service;
import com.imlehr.summer.beans.definition.BeanDefinition;
import com.imlehr.summer.beans.definition.BeanDefinitionHolder;
import com.imlehr.summer.beans.definition.BeanDefinitionRegistry;
import lombok.SneakyThrows;

import java.io.File;
import java.util.ArrayList;
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

    public void scan(String... basePackages) {
        for (String basePackage : basePackages) {
            doScan(basePackage);
        }
    }

    @SneakyThrows
    private void doScan(String basePackage) {
        //todo:这里通过ClassPath的方法去扫描加载这些类？目前就只能这个样子咯？反正类加载器先用默认的

        String basePackageUrl = classPath + File.separator + basePackage.replace(".", File.separator);

        List componentClasses = new ArrayList<Class>();

        recrusiveScan(new File(basePackageUrl), componentClasses);

        Class[] classes = new Class[componentClasses.size()];
        componentClasses.toArray(classes);
        registry.register(classes);


    }

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
                    if (hasTag(target)) {
                        componentClasses.add(target);
                    }
                }
            }
        }
    }


    private boolean hasTag(Class clazz) {
        return clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Repository.class) || clazz.isAnnotationPresent(Controller.class);
    }


}
