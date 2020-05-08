package com.imlehr.summer.utils;

import com.google.common.collect.Lists;
import com.imlehr.summer.test.scanner.component.MyComponent;
import lombok.SneakyThrows;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lehr
 * @create: 2020-05-06
 */
public class AspectJUtils {

    //todo 暂时还不支持单个方法解析的execute语法

    public static List<Class> parseAspectEL(String el)
    {
        if(el.startsWith("within"))
        {
            parseWithin(el);
        }
        if(el.startsWith("execute"))
        {
            parseExecute(el);
        }

        return Lists.newArrayList(MyComponent.class);
    }

    @SneakyThrows
    public static void parseWithin(String el)
    {
        char[] chars = el.toCharArray();

        boolean illegal = false;

        boolean subPack = false;

        boolean isPack = false;
        int i= 0;
        for(;i<el.length();i++)
        {
            if(('a'<= chars[i] && 'z' >= chars[i]) || 'A'<= chars[i] && 'Z' >= chars[i])
            {
                continue;
            }
            if(chars[i]=='.')
            {
                if(chars[i+1]=='.')
                {
                    subPack = true;
                    break;
                }
                if(chars[i+1]=='*')
                {
                    isPack = true;
                    break;
                }
            }
            illegal = true;
            break;
        }

        //读取到包名
        String nameStr = el.substring(0,i);

        if(illegal)
        {
            throw new Exception("nmd非法了！");
        }

        if(subPack)
        {
            //这个是读取到子包然后全部递归的情况

        }
        if(isPack)
        {
            //这个是目标对象下面是个包的情况
        }

        //最后才是锁定一个类的情况


        //com.test.spring.aop.pointcutexp.*
        //com.test.spring.aop.pointcutexp..
        //com.test.spring.aop.pointcutexp.Aclass
        //有+和@的暂时不做实现，写起来比较累

    }

    //*：匹配任何数量字符；
    //..：匹配任何数量字符的重复，如在类型模式中匹配任何数量子包；而在方法参数模式中匹配任何数量参数。
    //+：匹配指定类型的子类型；仅能作为后缀放在类型模式后边。




    // 特权级  返回类型 方法名（入参）    ..代表全部匹配
    // 前面一个星就是 全部特权级+返回类型

    @SneakyThrows
    public static void parseExecute(String el)
    {
        char[] chars = el.toCharArray();

        int i = 0;

        String deco = null;
        String ret = null;
        boolean all = false;
        if(chars[i]=='*')
        {
            //权限和返回值类型都直接匹配了
            all = true;
        }
        else
        {
            for(;i<el.length();i++)
            {
                if(chars[i]==' ')
                {
                    i++;
                    break;
                }

            }
             int stp = i;

            deco = el.substring(0,i);


            for(;i<el.length();i++)
            {
                if(chars[i]==' ')
                {
                    i++;
                    break;
                }

            }

            ret = el.substring(stp,i);

        }

        int namSt = i;

        for(;i<el.length();i++)
        {
            if(chars[i+1]=='(')
            {
                i++;
                break;
                //名字识别结束
            }
        }

        String methodName = el.substring(namSt,i);

        boolean allAgs = false;

        for(;i<el.length();i++)
        {
            if(chars[i]=='.'&&chars[i+1]=='.')
            {
                allAgs = true;
                break;
                //全参数匹配，识别完毕
            }
            
        }

    }
}
