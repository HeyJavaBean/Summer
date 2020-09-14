package com.imlehr.summer.test.scanner.service;

import com.imlehr.summer.core.annotation.Autowired;
import com.imlehr.summer.core.annotation.scan.Service;
import com.imlehr.summer.test.scanner.component.MyComponent;

@Service
public class MyService {

    @Autowired
    private MyComponent component;


    {
        System.out.println("Hey I'm the service");
    }


    public void says()
    {
        System.out.println("Hey there I'm saying");
    }
}
