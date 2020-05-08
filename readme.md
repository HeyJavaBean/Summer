# AOP阶段
参考文档：https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#spring-core
目前在试图模仿写一个简陋的aop功能

# 第一阶段
-------------
> 参考内容：https://juejin.im/post/5d455eabe51d4561fd6cb472

关于参数名字的问题：
若使用 Spring 框架，对于运行时获取参数名，Spring 提供了内建支持，对应的实现类为 DefaultParameterNameDiscoverer （javadoc）。该类先尝试用 Java 8 新的反射 API 获取方法参数名，若无法获取，则使用 ASM 库读取 class 文件的 LocalVariableTable，对应的代码分别为 StandardReflectionParameterNameDiscoverer 和 LocalVariableTableParameterNameDiscoverer。

我的方案：ASM

AspectJ语法解析：https://www.youtube.com/playlist?list=PLDcmCgguL9rxPoVn2ykUFc8TOpLyDU5gx
自己写了个自动机

------------------------
> 能够粗糙实现支持各种标签配Bean就可以了
- Component：自动扫描
- @Repository等Component升级版先不对功能上考虑
- Autowired：默认按照类型注入
- Resource通过 “CommonAnnotationBeanPostProcessor” 类实现依赖注入，先不考虑实现
- Inject :通过‘AutowiredAnnotationBeanPostProcessor’ 类实现的依赖注入 先不考虑实现
- ComponentScan:定义扫描    

现在粗糙完成了自动扫描的基本功能
自动扫描的bean name可能还有问题？？？
但是感觉整个代码的结构一团乱....

改天来个循环依赖？？？

原版的扫描是在一个PostProcessor里工作的：ConfigurationClassPostProcessor的processConfigBeanDefinitions
我就先不这样做了，以后开始设计处理器的时候再统一改，现在能跑就ok

为什么原版除了beanDefinitionMap还要故意弄一个List来放名字？？？

AliasFor的具体玩法？？？


![](https://user-gold-cdn.xitu.io/2019/10/18/16ddda0ffaad67f1?imageslim)
 
 
 