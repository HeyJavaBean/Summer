package com.imlehr.summer.context;

import com.imlehr.summer.beans.AopConfig;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Lehr
 * @create: 2020-05-06
 */
public class AopHandler implements InvocationHandler {

    private Object target;
    private AopConfig aopConfig;

    public AopHandler(Object target, AopConfig aopConfig) {
        this.target = target;
        this.aopConfig = aopConfig;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        invokeAop(aopConfig.getBeforeMethod());

        // 执行目标对象方法
        Object returnValue = method.invoke(target, args);

        invokeAop(aopConfig.getAfterMethod());

        return returnValue;
    }

    @SneakyThrows
    private void invokeAop(Method method) {
        if (method != null) {
            method.invoke(aopConfig.getEntity());
        }

    }
}
