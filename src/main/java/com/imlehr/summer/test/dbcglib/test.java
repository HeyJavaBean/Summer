package com.imlehr.summer.test.dbcglib;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Lehr
 * @create: 2020-05-07
 */
public class test {

    public static void main(String[] args) {
//        Object proxy = ProxyFactory.create().getProxy(new SayHello());
//        proxy.toString();

        Student lehr = new Student().setName("Lehr");
        Student lerie = lehr;
        lerie = new Student().setName("Lerie");
        System.out.println(lehr);

    }


    static class SayHello {

        @Override
        public String toString() {
            System.out.println("Hey It's Me");
            return "hello cglib !";
        }
    }

    @Data
    @Accessors(chain = true)
    static
    class Student{
        private String name;
    }
}
