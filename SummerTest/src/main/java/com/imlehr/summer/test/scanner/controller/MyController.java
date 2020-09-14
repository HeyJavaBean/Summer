package com.imlehr.summer.test.scanner.controller;

import com.imlehr.summer.core.annotation.scan.Controller;
import com.imlehr.summer.web.bind.annotation.RequestMapping;

@Controller
public class MyController {
    {
        System.out.println("Hey I'm the controller");
    }


    @RequestMapping(name = "heylehr")
    public String test()
    {
        return "Hey There!";
    }

    @RequestMapping(name = "")
    public String test2()
    {
        return "Welcome to my SpringMVC";
    }
}
