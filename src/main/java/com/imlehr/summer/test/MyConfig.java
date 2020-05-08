package com.imlehr.summer.test;

import com.imlehr.summer.annotation.*;

/**
 * @author Lehr
 * @create: 2020-04-26
 */
@Configuration
@ComponentScan("com.imlehr.summer.test.scanner")
public class MyConfig {

    @Bean
    @Order(12)
    public Person lehr()
    {
        return new Person().setAge("20").setName("Lehr");
    }

    @Lazy
    @Order(1)
    @Bean(name = "Bruce")
    public Person person()
    {
        return new Person().setAge("20").setName("Bruce");
    }

    @Scope("prototype")
    @Bean(name = "people")
    public Person people01()
    {
        return new Person().setAge("22").setName("people");
    }

}
