package com.imlehr.summer.test;

import com.imlehr.summer.core.context.AnnotationConfigApplicationContext;
import com.imlehr.summer.core.context.ApplicationContext;
import com.imlehr.summer.test.scanner.component.MyComponent;
import com.imlehr.summer.test.scanner.controller.MyController;

import java.util.*;


/**
 * @author lehr
 */
public class SpringTest{

    public static void main(String[] args) throws Exception {

//        System.out.println("ApplicationContext构造开始");


        ApplicationContext ac = new AnnotationConfigApplicationContext(MyConfig.class);

//        System.out.println("ApplicationContext构造完成");
//
//        System.out.println(ac.getBean("lehr"));
//
//        System.out.println(ac.getBean("lehr"));
//
//        System.out.println(ac.getBean("Bruce"));
//
//        System.out.println(ac.getBean("people"));
//        System.out.println(ac.getBean("people"));
//        System.out.println(ac.getBean("people"));

        System.out.println("\n\n\n");

        System.out.println(((MyController)ac.getBean("com.imlehr.summer.test.scanner.controller.MyController")).test());
//        MyComponent bean = (MyComponent) ac.getBean("com.imlehr.summer.test.scanner.component.MyComponent");
//        Object bean1 = ac.getBean("com.imlehr.summer.test.scanner.service.MyService");
//        System.out.println(bean1==bean.service);

    }

}