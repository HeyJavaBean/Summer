package com.imlehr.summer.web.context;

import com.imlehr.summer.core.beans.factory.BeanFactory;
import com.imlehr.summer.core.context.ApplicationContext;

/**
 * @author Lehr
 * @create: 2020-09-09
 */
public interface WebApplicationContext extends ApplicationContext {

    BeanFactory getBeanFactory();


}
