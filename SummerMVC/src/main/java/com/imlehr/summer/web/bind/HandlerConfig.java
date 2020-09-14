package com.imlehr.summer.web.bind;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;

/**
 * @author Lehr
 * @create: 2020-09-09
 */
@Data
@Accessors(chain = true)
public class HandlerConfig {

    String uri;

    /**
     * 今天默认用get方法
     */
    String requestMethod = "GET";

    Object Bean;

    Method method;



}
