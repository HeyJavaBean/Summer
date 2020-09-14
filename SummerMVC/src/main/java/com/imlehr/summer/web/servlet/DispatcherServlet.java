package com.imlehr.summer.web.servlet;


import com.imlehr.summer.web.context.WebApplicationContext;
import com.imlehr.summer.web.lehr.MappingHandler;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自己瞎写的一个Dispatcher Servlet
 * 其实在Spring源码里面，是分为了spring-web和spring-webmvc两个模块的
 * 前者是通用的比如web flux也用到，后者里面只有一个servlet包
 * 这里我不太想实现那个复杂的servlet
 */
public class DispatcherServlet extends HttpServlet {


    private WebApplicationContext webApplicationContext;

    private MappingHandler mappingHandler;



    @Override
    public void init() throws ServletException {

        System.out.println("正在准备Dispatcher Servlet");

        mappingHandler = new MappingHandler(webApplicationContext);

    }

    public DispatcherServlet(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doService(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doService(req, resp);
    }

    /**
     * 真正处理业务逻辑
     */
    @SneakyThrows
    protected void doService(HttpServletRequest request, HttpServletResponse response) {

        doDispatch(request,response);

    }


    /**
     * 核心的业务逻辑
     * @param request
     * @param response
     */
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) {
        HttpServletRequest processedRequest = request;

        String requestURI = request.getRequestURI();

        mappingHandler.handle(requestURI,request,response);


    }


}