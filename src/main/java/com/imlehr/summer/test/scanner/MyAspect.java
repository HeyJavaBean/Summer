package com.imlehr.summer.test.scanner;


import com.imlehr.summer.annotation.aspect.*;
import com.imlehr.summer.annotation.scan.Component;
import com.imlehr.summer.aop.beans.JointPoint;
import com.imlehr.summer.aop.beans.ProceedingJointPoint;

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
    @Pointcut("within(com.imlehr.summer.test.scanner.component.MyComponent)")
    public void pointCut(){};




    @Before("pointCut()")
    public void beforeAop(JointPoint joinPoint)
    {
//        //可以读取到切入点的信息
//        //获取参数[Ljava.lang.Object;@45c8d09f  这里应该是一个数组...
//        System.out.println(joinPoint.getArgs().toString());
//        //获取类型：method-execution
//        System.out.println(joinPoint.getKind());
//        //获取方法签名 void com.imlehr.blog.summer.TestService.sayHey()
//        //joinPoint.getSignature().getName()就是获取到方法的名字
//        System.out.println(joinPoint.getSignature());
        System.out.println("真-前置通知");
    }


    //最终结果会被给到一个object的result里
    //如果类型不对就不会执行这个方法
    @AfterReturning(value = "pointCut()",returning = "result")
    public void arAop(String result)
    {
        System.out.println(result);
        System.out.println("真-返回通知");
    }


    @After("pointCut()")
    public void aferAop()
    {
        System.out.println("真-后置通知");
    }

    //如果异常类型不匹配就不会执行这个方法，所以可以用来指定处理不同异常的不同后置advice
    @AfterThrowing(value = "pointCut()",throwing = "ex")
    public void atAop(NullPointerException ex)
    {
        System.out.println("真-异常通知");
        System.out.println(ex.getMessage());
    }



    //必须要有一个procedingJointPointer去控制整个方法的执行，不然执行到先around然后直接after然后after returning就没有了 连before都没有
    //注意准备返回值
    //这个方法的坑点有点多，返回值int的时候还不能设置null之类的
    @Around("pointCut()")
    public Object aroundAop(ProceedingJointPoint joinPoint)
    {
        Object rt = null;
        System.out.println("真-开始环绕");
        //点了这个才能执行
        try {

            System.out.println("前置通知");
            //前置执行
            rt = joinPoint.proceed();
            //返回通知并没有在这里执行
            System.out.println("返回通知");
        } catch (Throwable throwable) {
            //异常
            System.out.println("异常通知");
        }finally {
            //后置通知
            System.out.println("后置通知");
            //后置通知执行
            System.out.println("真-结束环绕");
            return  rt;
            //返回通知执行？
        }


    }
}
