package com.imlehr.summer.context;

import com.imlehr.summer.beans.AopConfig;
import lombok.SneakyThrows;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author Lehr
 * @create: 2020-05-06
 */
public class AopMethodIntercreptor implements MethodInterceptor {




    private Object target;
    private AopConfig aopConfig;

    public AopMethodIntercreptor(Object target, AopConfig aopConfig) {
        this.target = target;
        this.aopConfig = aopConfig;
    }



    @SneakyThrows
    private void invokeAop(Method method) {
        if (method != null) {
            method.invoke(aopConfig.getEntity());
        }

    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

        invokeAop(aopConfig.getBeforeMethod());

        // 执行目标对象方法
        Object returnValue = method.invoke(target, args);

        invokeAop(aopConfig.getAfterMethod());

        return returnValue;
    }
}
