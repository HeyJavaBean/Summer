package com.imlehr.summer.core.aop.beans;

import com.github.houbb.asm.tool.reflection.AsmMethods;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lehr
 * @create: 2020-05-06
 * 用来记录某个Bean的AOP相关设定的
 */
@Data
@Accessors(chain = true )
public class AopConfig {

    private List<Class> aopClassess = new ArrayList<>();

    private Method beforeMethod;

    private Method afterMethod;

    private Method aroundMethod;

    private Method afterReturningMethod;

    private Method afterThrowingMethod;

    private Object entity;

    private String exceptionName;

    private String resultName;

    public void invokeBefore(JointPoint jointPoint)
    {
        invokeMethod(beforeMethod,jointPoint);
    }

    public void invokeAfter(JointPoint jointPoint)
    {
        invokeMethod(afterMethod,jointPoint);
    }


    @SneakyThrows
    public void invokeThrow(Exception e,JointPoint jointPoint)
    {
        if(afterThrowingMethod!=null)
        {
            ArrayList<Parameter> parameters = Lists.newArrayList(afterThrowingMethod.getParameters());
            if(parameters.size()<1)
            {
                afterThrowingMethod.invoke(entity);
            }

            Boolean hasException = false;
            Boolean hasJointPoint = false;

            for (Parameter p : parameters) {

                if(AsmMethods.getParamNamesByAsm(afterThrowingMethod).contains(exceptionName))
                {


                    //明日任务：
                    // TODO:增加排序，order标签
                    // TODO:优化代理
                    // TODO:二重代理
                    // TODO:参考下SpringAOP




                    if(p.getType().isAssignableFrom(e.getClass()))
                    {
                        hasException = true;
                    }
                }
                if(p.getType().equals(JointPoint.class))
                {
                    hasJointPoint = true;
                }
            }

            if(hasException && hasJointPoint)
            {
                afterThrowingMethod.invoke(entity,e,jointPoint);
            }
            if(hasException && !hasJointPoint)
            {
                afterThrowingMethod.invoke(entity,e);
            }
            if(!hasException && hasJointPoint)
            {
                afterThrowingMethod.invoke(entity,jointPoint);
            }


        }

    }



    public Object invokeAround(ProceedingJointPoint jointPoint)
    {
        if(aroundMethod==null)
        {
            Object result = null;
            invokeBefore(jointPoint);
            try{
                result = jointPoint.proceed();
                invokeAfter(jointPoint);
                invokeReturning(result,jointPoint);
            }catch (Exception e)
            {
                invokeAfter(jointPoint);
                invokeThrow(e,jointPoint);
            }
            finally {
                return result;
            }
        }
        else
        {
            Object result = null;
            try{
                result = aroundMethod.invoke(entity, jointPoint);
                invokeAfter(jointPoint);
                //todo 不知道这里需不需要考虑克隆备份拿到情况
                invokeReturning(result,jointPoint);
            }catch (Exception e)
            {
                invokeAfter(jointPoint);
                invokeThrow(e,jointPoint);
            }finally {
                return result;
            }

        }
    }


    @SneakyThrows
    public void invokeReturning(Object result,JointPoint jointPoint)
    {
        if(afterReturningMethod!=null)
        {
            ArrayList<Parameter> parameters = Lists.newArrayList(afterReturningMethod.getParameters());
            if(parameters.size()<1)
            {
                afterReturningMethod.invoke(entity);
            }

            Boolean hasResult = false;
            Boolean hasJointPoint = false;

            for (Parameter p : parameters) {

                //用了ASM-Tool
                if(AsmMethods.getParamNamesByAsm(afterReturningMethod).contains(resultName))
                {
                    //处理继承关系
                    if(result!=null&& p.getType().isAssignableFrom(result.getClass()))
                    {
                        hasResult = true;
                    }
                }

                if(p.getType().equals(JointPoint.class))
                {
                    hasJointPoint = true;
                }
            }

            if(hasResult && hasJointPoint)
            {
                afterReturningMethod.invoke(entity,result,jointPoint);
            }
            if(hasResult && !hasJointPoint)
            {
                afterReturningMethod.invoke(entity,result);
            }
            if(!hasResult && hasJointPoint)
            {
                afterReturningMethod.invoke(entity,jointPoint);
            }


        }

    }

    @SneakyThrows
    private void invokeMethod(Method method,JointPoint jointPoint)
    {
        if (method != null) {
            Parameter[] parameters = method.getParameters();
            if(parameters.length<1)
            {
                method.invoke(entity);
            }
            if(parameters.length==1)
            {
                //这里before系列只能有最多1个的入参
                method.invoke(entity,jointPoint);
            }
        }
    }


}
