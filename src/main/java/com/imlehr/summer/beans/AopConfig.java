package com.imlehr.summer.beans;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
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

    private Object entity;

}
