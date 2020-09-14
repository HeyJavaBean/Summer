package com.imlehr.summer.web.context;

import com.imlehr.summer.core.beans.factory.BeanFactory;
import com.imlehr.summer.core.context.AnnotationConfigApplicationContext;

import javax.servlet.ServletContext;

/**
 * @author Lehr
 * @create: 2020-09-09
 */
public class AnnotationConfigWebApplicationContext extends AnnotationConfigApplicationContext implements WebApplicationContext{



    @Override
    public BeanFactory getBeanFactory() {
        return super.getBeanFactory();
    }

    public void setServletContext(ServletContext container)
    {
        container.setAttribute("SummerRootContext",this);
    }

}
