实现简单的Spring框架

参考：
[手写Spring框架之IOC](https://blog.csdn.net/litianxiang_kaola/article/details/86647022)

### Ioc
IOC的实现思路如下:

- 首先有一个配置文件定义了应用的基础包, 也就是Java源码路径.
- 读取基础包名, 然后通过类加载器获取到应用中所有的Class对象, 存储到一个集合中.
- 获取应用中所有Bean (Controller和Service) 的Class对象, 通过反射创建实例, 然后存储到 Bean容器中.
- 遍历Bean容器中的所有Bean, 为所有带 @Autowired 注解的属性注入实例.
- IOC操作要在应用启动时就完成, 所以必须写在静态代码块中.
