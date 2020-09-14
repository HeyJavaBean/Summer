package com.imlehr.summer.core.aop.beans;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Lehr
 * @create: 2020-05-06
 */
@Data
@Accessors(chain = true )
public class JointPoint {

    private String signature;

    private Object[] args;





}
