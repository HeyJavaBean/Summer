# AOP阶段
参考文档：https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#spring-core
目前在试图模仿写一个简陋的aop功能

# 第一阶段
-------------
> 参考内容：https://juejin.im/post/5d455eabe51d4561fd6cb472
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
 
 
 