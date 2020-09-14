package com.imlehr.summer.core.aop;

import com.imlehr.summer.core.aop.beans.AopConfig;
import com.imlehr.summer.core.aop.beans.ProceedingJointPoint;
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



    @Override
    @SneakyThrows
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

        ProceedingJointPoint jointPoint = new ProceedingJointPoint().setAopConfig(aopConfig)
                .setArgs(args).setCoreMethod(method).setTarget(target);

        jointPoint.setSignature(method.toString());

        return aopConfig.invokeAround(jointPoint);
    }
}
