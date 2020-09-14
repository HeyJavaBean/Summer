package com.imlehr.summer.web;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Lehr
 * @create: 2020-09-09
 * 这个是通过SPI启动Spring模块的逻辑
 * 这里基本上是Spring的源码
 */
@HandlesTypes(WebApplicationInitializer.class)
public class SpringServletContainerInitializer implements ServletContainerInitializer {


    /**
     * 使用ServletContainerInitializer的要求：
     * 要按规则写一个配置文件然后写上全类名
     * 然后HandlesTypes里必须是个接口而且必须有实现类
     * 然后整个项目里必须有Servlet才能跑
     * 关于Servlet 3.0的这个 SPI 可以参见下面这个链接：
     * https://www.logicbig.com/tutorials/java-ee-tutorial/java-servlet/servlet-container-initializer-example.html
     *
     */

    @Override
    public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext) throws ServletException {

        System.out.println("Hey My Summer MVC");


        //扫描所有用户想实现的Initializer，准备一个容器
        List<WebApplicationInitializer> initializers = new LinkedList<WebApplicationInitializer>();

        //挨个扫描，如果是实现类（不是接口，不是抽象类），实例化然后放入list
        // todo 这个我不不知道WebApplicationInitializer.class.isAssignableFrom(waiClass)
        if (webAppInitializerClasses != null) {
            for (Class<?> waiClass : webAppInitializerClasses) {
                // Be defensive: Some servlet containers provide us with invalid classes,
                // no matter what @HandlesTypes says...
                if (!waiClass.isInterface() && !Modifier.isAbstract(waiClass.getModifiers()) &&
                        WebApplicationInitializer.class.isAssignableFrom(waiClass)) {
                    try {
                        initializers.add((WebApplicationInitializer) waiClass.newInstance());
                    }
                    catch (Throwable ex) {
                        throw new ServletException("Failed to instantiate WebApplicationInitializer class", ex);
                    }
                }
            }
        }

        //如果是空的就讲一下莫得
        if (initializers.isEmpty()) {
            System.out.println("Hey There's No Summer MVC Initializer!");
            return;
        }

        //todo 源码这里还有个给Initializer排序的函数我先不管了

        //挨个初始化，也就是用注解配置SpringMVC里你看到的那些东西了
        for (WebApplicationInitializer initializer : initializers) {
            initializer.onStartup(servletContext);
        }

    }
}
