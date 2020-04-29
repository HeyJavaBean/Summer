package com.imlehr.summer.test;

import com.imlehr.summer.context.ApplicationContext;
import com.imlehr.summer.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author lehr
 */
public class SpringTest{

    public static void main(String[] args) throws Exception {

        System.out.println("ApplicationContext构造开始");

        ApplicationContext ac = new AnnotationConfigApplicationContext(MyConfig.class);

        System.out.println("ApplicationContext构造完成");

        System.out.println(ac.getBean("lehr"));

        System.out.println(ac.getBean("lehr"));

        System.out.println(ac.getBean("Bruce"));

        System.out.println(ac.getBean("people"));
        System.out.println(ac.getBean("people"));
        System.out.println(ac.getBean("people"));

    }

}