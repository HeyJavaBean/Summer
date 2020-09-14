package com.imlehr.summer.core.context;


/**
 * 这个类代表应用上下文
 */
public interface ApplicationContext {

    Object getBean(String name);

}
