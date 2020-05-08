package com.imlehr.summer.test.dbcglib;

import org.apache.tools.ant.taskdefs.Sleep;

public class Point1 implements Chain.Point {

    @Override
    public Object proceed(Chain chain) {
        System.out.println("point 1 before");

        Object result = chain.proceed();

        System.out.println("point 1 after");
        return result;
    }
}