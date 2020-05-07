package com.imlehr.summer.test.scanner.component;

import com.imlehr.summer.annotation.Autowired;
import com.imlehr.summer.annotation.scan.Component;
import com.imlehr.summer.test.scanner.service.MyService;

@Component
public class MyComponent{


    @Autowired
    private MyService service;
    {
        System.out.println("Hey I'm the component");
    }

    public String sayHey()
    {
        //int a = 1/0;
        System.out.println("now im saying hey");
        return "nmd";
    }
}
