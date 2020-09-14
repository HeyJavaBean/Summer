package com.imlehr.summer.core.aop.dfs;

import com.imlehr.summer.core.aop.beans.AspectMeaning;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lehr
 * @create: 2020-05-09
 */
public class ExecuteDFS {

    private static int[][] matrix = new int[7][19];

    static {
        set(0,0,1);
        set(0,1,3);
        set(1,0,1);
        set(1,6,2);
        set(2,0,4);
        set(3,6,5);
        set(4,0,4);
        set(4,0,4);
        set(4,6,5);
        set(5,0,6);
        set(6,0,6);
        set(6,2,7);
        set(6,3,13);
        set(7,0,6);
        set(7,1,11);
        set(7,2,8);
        set(8,0,9);
        set(8,1,11);
        set(9,0,9);
        set(9,2,10);
        set(9,3,13);
        set(10,1,12);
        set(10,0,9);
        set(11,3,13);
        set(11,2,10);
        set(12,3,13);
        set(13,0,15);
        set(13,2,14);
        set(14,2,16);
        set(15,0,15);
        set(15,4,18);
        set(15,5,17);
        set(16,5,18);
        set(17,0,15);

    }


    private static void set(int h,int l,int value)
    {
        matrix[l][h] = value;
    }
    private static int get(int h,int l)
    {
        int matrix = ExecuteDFS.matrix[l][h];
        if(matrix==0 && h!=18)
        {
            return 99;
        }
        return matrix;
    }


    //*：匹配任何数量字符；
    //..：匹配任何数量字符的重复，如在类型模式中匹配任何数量子包；而在方法参数模式中匹配任何数量参数。
    //+：匹配指定类型的子类型；仅能作为后缀放在类型模式后边。


    // 特权级  返回类型 方法名（入参）    ..代表全部匹配
    // 前面一个星就是 全部特权级+返回类型

    //public void com.imlehr.test.Method(int,int)
    //* com.imlehr.test..*.myMethod(int,char,float)

    @SneakyThrows
    public static AspectMeaning parse(String el) {
        char[] input = el.toCharArray();
        int i = 0;
        int state = 0;

        /**
         * 权限修饰符
         */
        char[] modifier = new char[9];
        int modi = 0;

        /**
         * 方法返回值
         */
        char[] ret = new char[10];
        int reti = 0;

        /**
         * 包名
         */
        char[] pac = new char[40];
        int paci = 0;

        /**
         * 方法名
         */
        char[] rm = new char[40];
        int rmi = 0;

        /**
         * 参数类型
         */
        char[] ag1 = new char[10];
        char[] ag2 = new char[10];
        char[] ag3 = new char[10];
        char[] ag4 = new char[10];

        List<char[]> args = new ArrayList<>();
        args.add(new char[10]);


        int agi = 0;
        int agn = 0;

        boolean allags = false;

        boolean rc = false;

        boolean tp = false;

        while (i < el.length()) {
            state = move(state, input[i++]);
            if(state==3)
            {
                //这就代表走了D点，前面是*输入
            }
            if(state==1)
            {
                //B点
                //获取修饰符
                modifier[modi++] = input[i-1];
            }
            if(state==4)
            {
                //E点
                //获取返回值类型
                ret[reti++] = input[i-1];
            }
            if(state==6)
            {
                //G点
                //获取正常修饰的包名
                pac[paci++] = input[i-1];
            }
            if(state==7)
            {
                //G点
                //获取正常修饰的包名的那个分割的点点，当然，也可能直接获得方法
                pac[paci++] = input[i-1];
            }
            if(state==8)
            {
                //I点
                //识别到了..
                rc = true;
            }
            if(state==9)
            {
                rm[rmi++] = input[i-1];
            }
            if(state==16)
            {
                allags = true;
            }
            if(state==15)
            {
                args.get(agn)[agi++] = input[i-1];
            }
            if(state==17)
            {
                agi=0;
                agn++;
                args.add(new char[10]);
            }
            if(state==3)
            {
                tp = true;
            }

        }


        if(tp)
        {
            System.out.println("修饰符是：public / private / default / protected");
            System.out.println("返回值是：any type");
        }
        else
        {
            System.out.println("修饰符是："+new String(modifier));
            System.out.println("返回值是："+ new String(ret));

        }
        System.out.println("包名是："+ new String(pac));


        if(rc)
        {
            System.out.println("需要递归处理子包");
            System.out.println("需要递归查找的方法是："+ new String(rm));
        }
        if(allags)
        {
            System.out.println("支持所有同名方法，参数不限");
        }
        for (char[] arg : args) {
            System.out.println("参数类型："+new String(arg));
        }

        return null;

    }

    @SneakyThrows
    private static Integer move(int state, char input) {
        int inputNum = inputFx(input);
        Integer nextState = get(state,inputNum);
        if (nextState == null) {
            throw new Exception("illegal!!!");
        }
        return nextState;
    }



    private static Integer inputFx(char input) {
        if ((input <= 'z' && input >= 'a') || (input <= 'Z' && input >= 'A')) {
            return 0;
        }
        if (input == '*') {
            return 1;
        }
        if (input == '.') {
            return 2;
        }
        if (input == '(') {
            return 3;
        }
        if (input == ')') {
            return 4;
        }
        if (input == ',') {
            return 5;
        }
        if (input == ' ') {
            return 6;
        }


        return 99;
    }


}
