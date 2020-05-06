package com.imlehr.summer.utils;

import com.google.common.collect.Lists;
import com.imlehr.summer.test.scanner.component.MyComponent;

import java.util.List;

/**
 * @author Lehr
 * @create: 2020-05-06
 */
public class AspectJUtils {

    public static List<Class> parseAspectEL(String el)
    {
        return Lists.newArrayList(MyComponent.class);
    }


}
