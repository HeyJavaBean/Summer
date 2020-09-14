package com.imlehr.summer.test.init;


import com.imlehr.summer.test.MyConfig;
import com.imlehr.summer.web.WebApplicationInitializer;
import com.imlehr.summer.web.context.AnnotationConfigWebApplicationContext;
import com.imlehr.summer.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * @author lehr
 * 配置启动类，和Spring的MVC的规范是差不多的
 */
public class AppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext container) throws ServletException {

		//配置Dispatcher Servlet内部的IoC容器
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(MyConfig.class);
		ctx.setServletContext(container);
		//todo 这里还是有点迷...
		ctx.refresh();

		//把Servlet整到Tomcat里去
		ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(ctx));
		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");

	}

}

