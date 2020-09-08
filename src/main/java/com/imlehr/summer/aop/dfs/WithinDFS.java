package com.imlehr.summer.aop.dfs;

import com.google.common.collect.Lists;
import com.imlehr.summer.aop.beans.AspectMeaning;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lehr
 * @create: 2020-05-09
 * Within的粒度大小是类为单位的
 */
public class WithinDFS {

    //目前只实现了支持三种语法
    //com.test.spring.aop.pointcutexp.*
    //com.test.spring.aop.pointcutexp..
    //com.test.spring.aop.pointcutexp.Aclass
    //有+和@的暂时不做实现，写起来比较累



    private static Map<Integer, List<Integer>> myMultimap = new HashMap<>();

    static {
        myMultimap.put(0, Lists.newArrayList(1));
        myMultimap.put(1, Lists.newArrayList(1,2));
        myMultimap.put(2, Lists.newArrayList(1,3,4));
    }


    @SneakyThrows
    public static AspectMeaning parseWithin(String el) {
        char[] input = el.toCharArray();
        int i = 0;

        int state = 0;

        while (i < el.length()) {
            state = move(state, input[i++]);
        }


        if(state==1)
        {
            //返回一个具体类的名字
            System.out.println("只用扫描一个类");
            return new AspectMeaning().setClassName(el).setOnlyClass(true);
        }
        if(state==3){
            System.out.println("需要递归扫描子包");
            return new AspectMeaning().setPackageName(el.substring(0,el.length()-2)).setIsPackageRecrusive(true);
        }
        if(state==4)
        {
            System.out.println("直接扫描这个包");
            return new AspectMeaning().setPackageName(el.substring(0,el.length()-2));
        }

        System.out.println("炸了！");
        return null;
    }

    @SneakyThrows
    private static Integer move(int state, char input) {
        int inputNum = inputFx(input);
        Integer nextState = myMultimap.get(state).get(inputNum);
        if (nextState == null) {
            throw new Exception("illegal!!!");
        }
        return nextState;
    }



    private static Integer inputFx(char input) {
        if ((input <= 'z' && input >= 'a') || (input <= 'Z' && input >= 'A')) {
            return 0;
        }
        if (input == '.') {
            return 1;
        }
        if (input == '*') {
            return 2;
        }

        return 99;
    }


}
