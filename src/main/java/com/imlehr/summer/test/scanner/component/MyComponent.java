package com.imlehr.summer.test.scanner.component;

import com.imlehr.summer.annotation.Autowired;
import com.imlehr.summer.annotation.scan.Component;
import com.imlehr.summer.test.scanner.service.MyService;

@Component
public class MyComponent implements TestInterface{


    @Autowired
    private MyService service;
    {
        System.out.println("Hey I'm the component");
    }

    @Override
    public void ss()
    {
        service.says();
    }
}
