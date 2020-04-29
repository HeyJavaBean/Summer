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
但是感觉整个代码的结构一团乱....

改天来个循环依赖？？？

原版的扫描是在一个PostProcessor里工作的：ConfigurationClassPostProcessor的processConfigBeanDefinitions
我就先不这样做了，以后开始设计处理器的时候再统一改，现在能跑就ok

为什么原版除了beanDefinitionMap还要故意弄一个List来放名字？？？

AliasFor的具体玩法？？？


# 目前的疑惑
- IoC 和 DI 的理解？ 思路和具体实现的关系？
![](https://pic1.zhimg.com/v2-d1894656e55d9db98345b7f75c1c4260_r.jpg)
 
 
 