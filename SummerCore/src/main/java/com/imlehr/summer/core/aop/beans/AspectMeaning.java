package com.imlehr.summer.core.aop.beans;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Lehr
 * @create: 2020-05-06
 * 用于存放DFS解析后的结果信息
 */
@Data
@Accessors(chain = true )
public class AspectMeaning {



    private String packageName;

    private String className;

    private String methodName;

    private String methodModifier;

    private List<String> argsList;

    private Boolean isPackageRecrusive = false;

    private Boolean onlyClass = false;

}
