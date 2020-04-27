package com.imlehr.summer.test;

import com.imlehr.summer.annotation.Bean;
import com.imlehr.summer.annotation.Configuration;

/**
 * @author Lehr
 * @create: 2020-04-26
 */
@Configuration
public class MyConfig {

    @Bean
    public Person lehr()
    {
        return new Person().setAge("20").setName("Lehr");
    }

    @Bean(name = "Bruce",lazy = true)
    public Person person()
    {
        return new Person().setAge("20").setName("Bruce");
    }

    @Bean(name = "people", scope = "prototype")
    public Person people01()
    {
        return new Person().setAge("22").setName("people");
    }

}
