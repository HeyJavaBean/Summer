package com.imlehr.summer.test.scanner;


import com.imlehr.summer.annotation.aspect.Aspect;
import com.imlehr.summer.annotation.aspect.Before;
import com.imlehr.summer.annotation.aspect.Pointcut;
import com.imlehr.summer.annotation.scan.Component;

/**
 * @author Lehr
 * @create: 2020-05-06
 */
@Aspect
@Component
public class MyAspect {

    /**
     * 只是用来申明切点
     */
    @Pointcut("within(com.imlehr.summer.test.scanner.component)")
    public void pointCut(){};


    @Before("pointCut()")
    public void beforeAop()
    {
        System.out.println("Hey before! I'm one of the aop!");
    }

}
