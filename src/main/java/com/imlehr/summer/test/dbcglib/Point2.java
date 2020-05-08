package com.imlehr.summer.test.dbcglib;

import org.apache.tools.ant.taskdefs.Sleep;

public class Point2 implements Chain.Point {

    @Override
    public Object proceed(Chain chain) {
        System.out.println("point 2 before");

        Object result = chain.proceed();

        System.out.println("point 2 after");
        return result;
    }
}
