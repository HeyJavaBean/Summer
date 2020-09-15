破烂循环依赖，写了一堆烂代码...

参考文档：https://docs.spring.io/spring/docs/5.2.6.RELEASE/spring-framework-reference/core.html#spring-core
![](https://user-gold-cdn.xitu.io/2019/10/18/16ddda0ffaad67f1?imageslim)
参考内容：https://juejin.im/post/5d455eabe51d4561fd6cb472

关于参数名字的问题：
若使用 Spring 框架，对于运行时获取参数名，Spring 提供了内建支持，对应的实现类为 DefaultParameterNameDiscoverer （javadoc）。该类先尝试用 Java 8 新的反射 API 获取方法参数名，若无法获取，则使用 ASM 库读取 class 文件的 LocalVariableTable，对应的代码分别为 StandardReflectionParameterNameDiscoverer 和 LocalVariableTableParameterNameDiscoverer。
我的方案：ASM

AspectJ语法解析：https://www.youtube.com/playlist?list=PLDcmCgguL9rxPoVn2ykUFc8TOpLyDU5gx
自己写了个自动机


9.11 重构了AOP的过程，但是还是和源码不一样

 
 
 