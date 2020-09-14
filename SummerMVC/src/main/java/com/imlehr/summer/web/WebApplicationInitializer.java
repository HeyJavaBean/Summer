

package com.imlehr.summer.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * 这个是抄的SpringMVC源码的
 */
public interface WebApplicationInitializer {

	void onStartup(ServletContext servletContext) throws ServletException;

}
