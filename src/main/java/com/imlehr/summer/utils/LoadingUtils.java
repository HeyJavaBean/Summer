package com.imlehr.summer.utils;

import lombok.SneakyThrows;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lehr
 * @create: 2020-09-07
 * 用来通过包名类名找Class的
 */
public class LoadingUtils {

    //todo：个人认为可能会因为不同的路径而报错.... 到时候项目总体结构还要修改
    private static final String classPath = System.getProperty("user.dir") + File.separator + "build" + File.separator + "classes/java/main";

    /**
     * 利用目录查询url
     *
     * @param basePackage
     * @return
     */
    private static File getPackageFile(String basePackage) {
        //拼接出一个完整的url
        String basePackageUrl = classPath + File.separator + basePackage.replace(".", File.separator);

        return new File(basePackageUrl);
    }

    /**
     * 给一个包名，然后加载所有的类
     */
    @SneakyThrows
    public static List<Class> scanPackage(String basePackage) {

        //这是用于存放可能扫描到的类的容器
        List componentClasses = new ArrayList<Class>();

        File f = getPackageFile(basePackage);

        for (File file : f.listFiles()) {
            if (file.getName().endsWith(".class")) {
                //fixme 这段获取类名的写法，强烈需要改进
                String absolutePath = file.getAbsolutePath();
                String substring = absolutePath.substring(classPath.length() + 1, absolutePath.length() - 6);
                String packageName = substring.replace(File.separator, ".");
                Class<?> target = Class.forName(packageName);
                componentClasses.add(target);
            }

        }

        return componentClasses;

    }

    /**
     * 给一个包名，递归查询所有类
     *
     * @param basePackage
     * @return
     */
    @SneakyThrows
    public static List<Class> recrusivePackage(String basePackage) {

        //这是用于存放可能扫描到的类的容器
        List componentClasses = new ArrayList<Class>();

        File f = getPackageFile(basePackage);

        recrusiveScan(f, componentClasses);

        return componentClasses;

    }

    /**
     * 通过类名找类，很粗暴的一个包装
     *
     * @param name
     * @return
     */
    @SneakyThrows
    public static Class findClassByName(String name) {
        Class<?> target = Class.forName(name);
        return target;
    }


    /**
     * 给一个
     *
     * @param f
     * @param componentClasses
     */
    @SneakyThrows
    private static void recrusiveScan(File f, List componentClasses) {
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
                    componentClasses.add(target);
                }
            }
        }
    }

}
