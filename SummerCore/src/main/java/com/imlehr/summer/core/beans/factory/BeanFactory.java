package com.imlehr.summer.core.beans.factory;

import com.imlehr.summer.core.annotation.Bean;
import com.imlehr.summer.core.annotation.ComponentScan;
import com.imlehr.summer.core.annotation.Configuration;
import com.imlehr.summer.core.annotation.aspect.*;
import com.imlehr.summer.core.aop.ProxyManager;
import com.imlehr.summer.core.beans.definition.BeanDefinition;
import com.imlehr.summer.core.beans.definition.BeanDefinitionHolder;
import com.imlehr.summer.core.beans.object.ObjectFactory;
import com.imlehr.summer.core.beans.definition.BeanDefinitionRegistry;
import lombok.SneakyThrows;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lehr
 * @create: 2020-04-27
 */
public class BeanFactory {

    /**
     * 原版也是用的一个concurrentHashmap
     */
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private ProxyManager proxyManager;

    private final BeanDefinitionRegistry registry;

    public BeanFactory(BeanDefinitionRegistry registry) {
        //todo 这里其实因为proxy里要用到getBean方法,还不得不额外提供了一个放入单例池的公开方法，但是我感觉这样设计组合似乎不太好
        proxyManager = new ProxyManager(this);
        this.registry = registry;
        //这里还需要注册一些processor但是我懒得写了
    }


    public void putIntoSingletonObjects(String name,Object proxy)
    {
        singletonObjects.put(name,proxy);
    }

    /**
     * 把bdh拆开然后放到自己的map里
     * @param bdh
     */
    public void registerBeanDefinition(BeanDefinitionHolder bdh) {
        beanDefinitionMap.put(bdh.getBeanName(), bdh.getBeanDefinition());
    }


    /**
     * 把所有的BeanDefinition给实例化
     * 这里我名字和下面的部分结构是仿照Spring源码起名的
     * 暂时不支持工厂bean的使用！！！
     */
    public void preInstantiateSingletons() {
        //是单例且非懒加载的，都挨个实例化
        beanDefinitionMap.forEach((name, beanDefinition) ->
        {
            if (beanDefinition.isSingleton() && beanDefinition.notLazy()) {
                //spring万恶的getBean加载方法
                getBean(beanDefinition);
            }

        });
    }


    /**
     * 一级缓存，用于存放完全初始化好的 bean，从该缓存中取出的 bean 可以直接使用
     */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    /**
     * 二级缓存 存放原始的 bean 对象（尚未填充属性），用于解决循环依赖
     */
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    /**
     * 三级缓存 存放 bean 工厂对象，用于解决循环依赖
     */
    private final Map<String, ObjectFactory> singletonFactories = new HashMap<>(16);


    /**
     * new完了但是还没有填写信息的情况
     */
    private final Set<String> singletonsCurrentlyInCreation =
            Collections.newSetFromMap(new ConcurrentHashMap<>(16));


    public boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }


    /**
     * 给一个BeanDefinition然后实例化后返回Bean
     * 这里就是重点了！！！
     *
     * @return
     */
    @SneakyThrows
    public Object getBean(BeanDefinition beanDefinition) {

        //源码这里还有个名称合法检查，因为他有FactoryBean的情况
        String beanName = beanDefinition.getBeanName();
        //首先从缓存池里尝试获取，这里就会走一级二级三级
        Object sharedInstance = getSingleton(beanName);

        //如果说是获取到了（但是有可能是个半成品）
        if (sharedInstance != null) {
            //使用某个方法完善实例化?

        } else {
            //开始实例化
            singletonsCurrentlyInCreation.add(beanName);
            sharedInstance = doCreateBean(beanDefinition);
            singletonObjects.put(beanName,sharedInstance);
            singletonsCurrentlyInCreation.remove(beanName);
        }

        return sharedInstance;
    }

    private Object createBeanInstance(BeanDefinition beanDefinition)
    {
        //new出对象
        ObjectFactory objectFactory = ObjectFactory.getFactory(beanDefinition);
        //放入到三级缓存里去
        singletonFactories.put(beanDefinition.getBeanName(), objectFactory);
        Object bean = objectFactory.getObject();
        return bean;
    }


    private void populateBean(BeanDefinition beanDefinition,Object bean)
    {
        List<Field> autowireList = beanDefinition.getAutowireList();
        if(autowireList!=null && !autowireList.isEmpty())
        {
            autowireList.forEach(ab->
            {
                beanDefinitionMap.values().forEach(bd->
                {
                    if(bd.getBeanClass().equals(ab.getType()))
                    {
                        Object field = getBean(bd);
                        try {
                            ab.setAccessible(true);
                            ab.set(bean,field);
                        } catch (IllegalAccessException e) {
                            System.out.println("属性注入出错了我靠");
                            e.printStackTrace();
                        }
                    }
                });
            });
        }
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {

        //创建对象
        Object instanceWrapper = createBeanInstance(beanDefinition);

        //注入属性
        populateBean(beanDefinition,instanceWrapper);

        //似乎是在这里做AOP
        initializeBean(instanceWrapper);

        //写的简略一点，创建代理对象实际上是应该在这里来完成的

        return instanceWrapper;
    }


    private void initializeBean(Object bean)
    {
        //todo 这个功能还暂时没有实现
    }


    public Object createProto(BeanDefinition beanDefinition)
    {clear
        ObjectFactory objectFactory = ObjectFactory.getFactory(beanDefinition);

        Object bean = objectFactory.getObject();
        List<Field> autowireList = beanDefinition.getAutowireList();
        if(autowireList!=null && !autowireList.isEmpty())
        {
            autowireList.forEach(ab->
            {
                beanDefinitionMap.values().forEach(bd->
                {
                    if(bd.getBeanClass().equals(ab.getType()))
                    {
                        Object field = getBean(bd);
                        try {
                            ab.setAccessible(true);
                            ab.set(bean,field);
                        } catch (IllegalAccessException e) {
                            System.out.println("属性注入出错了我靠");
                            e.printStackTrace();
                        }
                    }
                });
            });
        }


        return bean;

    }

    //todo 一个不太好的对外暴露的临时设计
    public Map<String,BeanDefinition> getAlls()
    {
        return beanDefinitionMap;
    }


    /**
     * 这部分基本上是沿用的Spring的源码，DefaultSingletonBeanRegistry部分
     * @param beanName
     * @return
     */
    private Object getSingleton(String beanName) {
        //先从一级缓存池里尝试获取
        Object singletonObject = this.singletonObjects.get(beanName);
        //如果是空的 或者说是正在创建的状态
        if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {

            //从二级缓存early里获取提前曝光的内容
            singletonObject = this.earlySingletonObjects.get(beanName);
            if (singletonObject == null) {
                //如果还是空的，就从 三级缓存里去获取，然后获取到了之后把他放到二级缓存里去
                ObjectFactory singletonFactory = this.singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    this.earlySingletonObjects.put(beanName, singletonObject);
                    this.singletonFactories.remove(beanName);
                }

            }
        }
        return singletonObject;
    }




    /**
     * 按照名称查找一个Bean
     *
     * @param name
     * @return
     */
    public Object findBean(String name) {
        return singletonObjects.get(name);
    }


    /**
     * 给一个名字获取BeanDefinition
     *
     * @param name
     * @return
     */
    public BeanDefinition getBeanDefinition(String name) {
        return beanDefinitionMap.get(name);
    }

    /**
     * 初始化之前已经加载了的东西
     */
    public void preInit() {
        beanDefinitionMap.values().forEach(bd->getBean(bd));
    }


    /**
     * 可以理解为刷新配置文件，读取所有的类
     * （但是这里似乎不支持二次刷新，我还没试过）
     * 从配置类里面获取到Bean
     * 这里是完善所有bean的注册内容
     */
    @SneakyThrows
    public void refresh() {

         beanDefinitionMap.forEach((name, candidate) ->
        {
            //选出有Configuration注解的类，进行处理
            if (candidate.getBeanClass().isAnnotationPresent(Configuration.class)) {
                //先解析用方法和@Bean配置的Bean
                parserConfig(candidate);
                //然后再通过扫描器来找要扫描的包来注册
                scanComponents(candidate);
            }
        });
    }

    /**
     * 把配置类里直接配置的Bean给解析出来，变为BeanDefinition去注册
     * @param configBean
     */
    @SneakyThrows
    private void parserConfig(BeanDefinition configBean) {

        //拿到类本体
        Class beanClass = configBean.getBeanClass();

        List<Method> beans = new ArrayList<>();

        // 遍历所有方法，通过注解找出来有bean的方法
        for (Method method : beanClass.getDeclaredMethods()) {

            if (method.isAnnotationPresent(Bean.class)) {
                beans.add(method);
            }
        }

        //交给IoC容器去注册这些内容，转化为BeanDefinition
        registry.registBean(beans,getBean(configBean));

    }

    /**
     * 把想通过注解注册的Bean找出来，整成BeanDefinition然后注册
     * @param config
     */
    public void scanComponents(BeanDefinition config) {
        //获取配置类本体
        Class beanClass = config.getBeanClass();
        //寻找是否有Scan解析标签
        boolean canScan = beanClass.isAnnotationPresent(ComponentScan.class);
        //开始扫描解析
        //todo 这里暂时懒得去实现ComponentScans这种复合功能
        if (canScan) {
            //获取标签
            ComponentScan componentScan = (ComponentScan) beanClass.getAnnotation(ComponentScan.class);
            //扫描到标签里的包路径
            String[] basePackages = componentScan.value();
            if (basePackages.length < 1) {
                //如果是空的，就默认包是当前的
                String packageName = beanClass.getPackageName();
                basePackages = new String[]{packageName};
            }
            //让IoC Context来做扫描，其实就是调用扫描器
            this.registry.scan(basePackages);
        }

    }


    /**
     * 读取切面配置文件，提前生成代理对象，我有点记不得Spring是不是这个顺序了
     */
    public void getProxy()
    {
        beanDefinitionMap.values().forEach(bd->
        {
            if(bd.getBeanClass().isAnnotationPresent(Aspect.class))
            {
                proxyManager.doProxy(bd);
            }
        });
    }


    /**
     * 按照Order标签对BeanDefinition进行排序
     * 默认是最低优先级,值越小优先级越高
     */
    public void sortBeanDefinition()
    {

        List<Map.Entry<String,BeanDefinition>> list = new ArrayList<Map.Entry<String,BeanDefinition>>(beanDefinitionMap.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,BeanDefinition>>() {
            //小的排前面
            @Override
            public int compare(Map.Entry<String, BeanDefinition> o1,
                               Map.Entry<String, BeanDefinition> o2) {
                return o1.getValue().getOrder() < (o2.getValue().getOrder())?-1:1;
            }
        });
    }

}
