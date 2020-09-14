package com.imlehr.summer.core.annotation;


import java.lang.annotation.*;

/**
 * . 扫描指定类文件
 *    @ComponentScan(basePackageClasses = Person.class)
 * 2. 扫描指定包，使用默认扫描规则，即被@Component, @Repository, @Service, @Controller或者已经声明过@Component自定义注解标记的组件；
 *    @ComponentScan(value = "com.yibai")
 * 3. 扫描指定包，加载被@Component注解标记的组件和默认规则的扫描（因为useDefaultFilters默认为true）
 *    @ComponentScan(value = "com.yibai", includeFilters = { @Filter(type = FilterType.ANNOTATION, value = Component.class) })
 * 4. 扫描指定包，只加载Person类型的组件
 *    @ComponentScan(value = "com.yibai", includeFilters = { @Filter(type = FilterType.ASSIGNABLE_TYPE, value = Person.class) }, useDefaultFilters = false)
 * 5. 扫描指定包，过滤掉被@Component标记的组件
 *    @ComponentScan(value = "com.yibai", excludeFilters = { @Filter(type = FilterType.ANNOTATION, value = Component.class) })
 * 6. 扫描指定包，自定义过滤规则
 *    @ComponentScan(value = "com.yibai", includeFilters = { @Filter(type = FilterType.CUSTOM, value = ColorBeanLoadFilter.class) }, useDefaultFilters = true)
 *   现在暂时设置的非常非常简单.....
 *   如果不去填写的话，在解析的时候会默认变成当前这个类所在的包以及以下的扫描范围
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(ComponentScans.class)
public @interface ComponentScan {

	String[] value() default {};


}
