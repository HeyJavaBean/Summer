package com.imlehr.summer.test.scanner.service;

import com.imlehr.summer.annotation.Autowired;
import com.imlehr.summer.annotation.Repository;
import com.imlehr.summer.annotation.Service;
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
        System.out.println("loca loca vanitosa");
    }
}
