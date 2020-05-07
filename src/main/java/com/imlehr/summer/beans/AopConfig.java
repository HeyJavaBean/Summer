package com.imlehr.summer.beans;

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
                //todo exceptionName.equals(p.getName()) 解决不了名字的问题
                if(true)
                {
                    //todo 这里处理不了继承关系
                    if(p.getType().equals(e.getClass()))
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
                //todo 暂时无法解决获取参数名字的问题：resultName.equals(p.getName())
                if(true)
                {
                    //todo 这里处理不了继承关系
                    if(result!=null&& p.getType().equals(result.getClass()))
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
