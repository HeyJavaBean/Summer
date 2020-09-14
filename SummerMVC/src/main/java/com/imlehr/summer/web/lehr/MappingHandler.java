package com.imlehr.summer.web.lehr;

import com.imlehr.summer.core.annotation.scan.Controller;
import com.imlehr.summer.core.annotation.scan.RestController;
import com.imlehr.summer.core.beans.definition.BeanDefinition;
import com.imlehr.summer.web.bind.HandlerConfig;
import com.imlehr.summer.web.bind.annotation.RequestMapping;
import com.imlehr.summer.web.context.WebApplicationContext;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Lehr
 * @create: 2020-09-09
 */
public class MappingHandler {

    private WebApplicationContext webApplicationContext;


    private Set<HandlerConfig> handlers = new HashSet<>();

    public MappingHandler(WebApplicationContext webApplicationContext)
    {
        this.webApplicationContext = webApplicationContext;
        scanHandler();
    }

    /**
     * 提取出Controller
     * 目前只支持@Controller的方式，其实真正版本还有个继承HttpController那个
     */
    private void scanHandler()
    {
        //从单例池里获取到有Controller标签的Bean
        Map<String, BeanDefinition> alls = webApplicationContext.getBeanFactory().getAlls();

        alls.forEach((name,bd)->
        {
            //找到带有Controller标签的BeanDefinition
            Class<? extends BeanDefinition> clazz = bd.getBeanClass();
            if(clazz.isAnnotationPresent(Controller.class)|| clazz.isAnnotationPresent(RestController.class))
            {
                Object bean = webApplicationContext.getBean(name);
                doMapping(bean);
            }
        });
    }

    private void doMapping(Object bean)
    {
        Class clazz = bean.getClass();
        String classMapping = "";
        if(clazz.isAnnotationPresent(RequestMapping.class))
        {
            RequestMapping annotation = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
            classMapping = annotation.name();
        }
        Method[] methods = clazz.getMethods();


        for (Method m : methods) {

            HandlerConfig handlerConfig = handleMethod(m);
            if(handlerConfig!=null)
            {
                handlerConfig.setMethod(m);
                handlerConfig.setBean(bean);
                if(classMapping.length()>1)
                {
                    String realUri = gather(classMapping,handlerConfig.getUri());
                    handlerConfig.setUri(realUri);
                }
                handlers.add(handlerConfig);
            }
        }
    }

    private String gather(String classMapping,String handlerConfig)
    {
        if(classMapping.endsWith("/"))
        {
            classMapping = classMapping.substring(0,classMapping.length()-1);
        }
        if(!classMapping.startsWith("/"))
        {
            classMapping = classMapping+"/";
        }

        if(handlerConfig.endsWith("/"))
        {
            handlerConfig = handlerConfig.substring(0,classMapping.length()-1);
        }
        if(handlerConfig.startsWith("/"))
        {
            handlerConfig = handlerConfig.substring(1);
        }

        return classMapping + "/" + handlerConfig;
    }


    private HandlerConfig handleMethod(Method m)
    {
        if(m.isAnnotationPresent(RequestMapping.class))
        {
            RequestMapping annotation = m.getAnnotation(RequestMapping.class);
            String methodUri = annotation.name();

            return new HandlerConfig().setUri(methodUri);
        }

        return null;
    }

    private HandlerConfig checkUri(String uri,String contextPath)
    {

        //todo  以后这里还需要具体实现一套支持ant风格的匹配

        //这里就先暴力做了
        for (HandlerConfig h : handlers) {
            if((contextPath+"/"+h.getUri()).equals(uri))
            {
                return h;
            }
        }
        return null;
    }

    @SneakyThrows
    public void handle(String requestURI, HttpServletRequest request, HttpServletResponse response) {

        HandlerConfig handlerConfig = checkUri(requestURI,request.getContextPath());

        //这里是错误逻辑处理，写的比较水
        if(handlerConfig==null)
        {
            response.setStatus(404);
            return ;
        }

        //正确逻辑，调用反射方法
        Method method = handlerConfig.getMethod();


        //todo 现在暂时不接受传参
        Object invoke = method.invoke(handlerConfig.getBean(), null);

        //todo 目前不支持返回对象
        response.getWriter().write(invoke.toString());
    }
}
