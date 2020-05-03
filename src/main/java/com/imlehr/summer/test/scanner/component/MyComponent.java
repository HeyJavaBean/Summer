package com.imlehr.summer.test.scanner.component;

import com.imlehr.summer.annotation.Autowired;
import com.imlehr.summer.annotation.Component;
import com.imlehr.summer.test.scanner.service.MyService;

@Component
public class MyComponent {


    @Autowired
    private MyService service;

    {
        System.out.println("Hey I'm the component");
    }

    public void ss()
    {
        service.says();
    }
}
