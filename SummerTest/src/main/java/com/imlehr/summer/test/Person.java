package com.imlehr.summer.test;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Lehr
 * @create: 2020-04-26
 */
@Data
@Accessors(chain = true)
public class Person {

    private String name;
    private String age;


    {
        System.out.println("Person正在构造ing");
    }

}
