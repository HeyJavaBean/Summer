package com.imlehr.summer.aop.beans;

import com.imlehr.summer.aop.beans.AopConfig;
import com.imlehr.summer.aop.beans.JointPoint;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;

/**
 * @author Lehr
 * @create: 2020-05-06
 */
@Getter
@Setter
@Accessors(chain = true )
public class ProceedingJointPoint  extends JointPoint {

    private Method coreMethod;

    private Object[] args;

    private Object target;

    private AopConfig aopConfig;


    public Object proceed() throws Exception
    {
        if(aopConfig.getAroundMethod()!=null)
        {
            aopConfig.invokeBefore(this);
        }
        return coreMethod.invoke(target, args);
    }

}
