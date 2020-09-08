package com.imlehr.summer.aop;

import com.google.common.collect.Lists;
import com.imlehr.summer.aop.beans.AspectMeaning;
import com.imlehr.summer.aop.dfs.ExecuteDFS;
import com.imlehr.summer.aop.dfs.WithinDFS;
import com.imlehr.summer.utils.LoadingUtils;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.List;


/**
 * @author Lehr
 * @create: 2020-05-06
 */
public class AspectJUtils {

    //todo 暂时还不支持单个方法解析的execute语法


    @SneakyThrows
    public static List<Class> parseAspectEL(String el) {

        //存放一些类
        List<Class> proxyClasses = Lists.newArrayList();
        AspectMeaning aspectMeaning = null;
        if (el.startsWith("within")) {
            aspectMeaning = WithinDFS.parseWithin(el.substring(7, el.length() - 1));

        }
        if (el.startsWith("execute")) {
            //todo: 暂时没有完成实现
            aspectMeaning = ExecuteDFS.parse(el.substring(8, el.length() - 1));
        }

        //todo:语法正确但是目标不存在的情况会爆炸！！！

        //目前下面的写法是按照Within语法解析的
        if (aspectMeaning.getOnlyClass()) {
            Class target = LoadingUtils.findClassByName(aspectMeaning.getClassName());
            proxyClasses.add(target);
            return proxyClasses;
        }
        if(aspectMeaning.getIsPackageRecrusive()){
            //递归处理包
            proxyClasses = LoadingUtils.recrusivePackage(aspectMeaning.getPackageName());
        }
        else
        {
            //单个处理包
            proxyClasses = LoadingUtils.scanPackage(aspectMeaning.getPackageName());
        }

        return proxyClasses;
    }

    @Test
    public void executeTest() {
        //todo Execute目前DFS大概写好了但是后续处理还没有设计好
        //String el = "public void com.imlehr.test.Method(int,int)";
        String el = "* com.imlehr.test..*.myMethod(int,char,float)";
        System.out.println("开始");
        ExecuteDFS.parse(el);
        System.out.println("识别成功");


//        String el = "within(com.imlehr.summer.test.scanner..)";

//        List<Class> classes = parseAspectEL(el);



    }


}
