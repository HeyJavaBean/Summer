package com.imlehr.summer.core.utils;

import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lehr
 * @create: 2020-09-07
 * 用来通过包名类名找Class的
 */
public class LoadingUtils {

    private static final String classPath;

    static
    {
        String packageName = "";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = "";
        URL url = classLoader.getResource(path);
        String temp = url.getPath();
        //todo 我确实处理不了中文的情况了
        classPath = temp.replace("%e6%a1%8c%e9%9d%a2","桌面");
    }

    /**
     * 利用目录查询url
     *
     * @param basePackage
     * @return
     */
    private static File getPackageFile(String basePackage) {
        //拼接出一个完整的url
        String basePackageUrl = classPath + basePackage.replace(".", File.separator);

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
                String absolutePath = file.getAbsolutePath();
                String substring = absolutePath.substring(classPath.length(), absolutePath.length() - 6);
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
                    String absolutePath = file.getAbsolutePath();
                    String substring = absolutePath.substring(classPath.length(), absolutePath.length() - 6);
                    String packageName = substring.replace(File.separator, ".");
                    Class<?> target = Class.forName(packageName);
                    componentClasses.add(target);
                }
            }
        }
    }

}
