package com.imlehr.summer.test.dbcglib;

import java.util.List;

public class Chain {
    private List<Point> list;
    private int index = -1;
    private Object target;

    public Chain(List<Point> list, Object target) {
        this.list = list;
        this.target = target;
    }

    public Object proceed() {
        Object result;
        if (++index == list.size()) {
            result = (target.toString());
            System.err.println("Target Method invoke result : " + result);
        } else {
            Point point = list.get(index);
            result = point.proceed(this);
        }
        return result;
    }
    interface Point {
        Object proceed(Chain chain);
    }
}