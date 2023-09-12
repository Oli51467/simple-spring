package com.sdu.springframework.core.manager;

import com.sdu.springframework.core.annotation.Aspect;
import com.sdu.springframework.core.annotation.Service;
import com.sdu.springframework.core.proxy.Proxy;
import com.sdu.springframework.core.proxy.ProxyAspect;
import com.sdu.springframework.core.proxy.ProxyFactory;
import com.sdu.springframework.core.proxy.TransactionProxy;
import com.sdu.springframework.core.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 框架中所有Bean的实例都是从Bean容器中获取，然后再执行该实例的方法。
 * 初始化AOP框架实际上就是用代理对象覆盖掉Bean容器中的目标对象，根据目标类的Class对象从Bean容器中获取到的就是代理对象，从而达到了对目标对象增强的目的
 */
@Slf4j
public final class AopManager {

    static {
        try {
            // 切面类-目标类集合的映射
            Map<Class<?>, Set<Class<?>>> aspectMap = createAspectMap();
            // 目标类-切面对象列表的映射
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(aspectMap);
            // 把切面对象织入到目标类中, 创建代理对象
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                // 目标bean
                Class<?> targetClass = targetEntry.getKey();
                // 获取到的是所有代理对该切面的增强
                List<Proxy> proxyList = targetEntry.getValue();
                // 通过代理工厂一次性将所有增强实现加入切面
                Object proxy = ProxyFactory.createProxy(targetClass, proxyList);
                // 覆盖Bean容器里目标类对应的实例, 下次从Bean容器获取的就是代理对象了
                BeanManager.setBean(targetClass, proxy);
            }
        } catch (Exception e) {
            log.error("aop failure", e);
        }
    }

    /**
     * 获取切面类-目标类集合的映射
     */
    private static Map<Class<?>, Set<Class<?>>> createAspectMap() throws Exception {
        Map<Class<?>, Set<Class<?>>> aspectMap = new HashMap<>();
        addAspectProxy(aspectMap);
        addTransactionProxy(aspectMap);
        return aspectMap;
    }

    /**
     * 获取普通切面类-目标类集合的映射
     */
    private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> aspectMap) throws Exception {
        // 所有实现了ProxyAspect抽象类的切面
        Set<Class<?>> aspectClassSet = ClassManager.getSuperClassSet(ProxyAspect.class);
        for (Class<?> aspectClass : aspectClassSet) {
            if (aspectClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = aspectClass.getAnnotation(Aspect.class);
                // 与该切面对应的目标类集合
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                aspectMap.put(aspectClass, targetClassSet);
            }
        }
    }

    /**
     * 获取事务切面类-目标类集合的映射
     */
    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> aspectMap) {
        Set<Class<?>> serviceClassSet = ClassManager.getAnnotationClassSet(Service.class);
        aspectMap.put(TransactionProxy.class, serviceClassSet);
    }

    /**
     * 根据@Aspect定义的包名和类名去获取对应的目标类集合
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception {
        Set<Class<?>> targetClassSet = new HashSet<>();
        // 包名
        String pkg = aspect.pkg();
        // 类名
        String cls = aspect.clazz();
        // 如果包名与类名均不为空，则添加指定类
        if (!pkg.equals("") && !cls.equals("")) {
            targetClassSet.add(Class.forName(pkg + "." + cls));
        } else if (!pkg.equals("")) {
            // 如果包名不为空, 类名为空, 则添加该包名下所有类
            targetClassSet.addAll(ClassUtil.getClasses(pkg));
        }
        return targetClassSet;
    }

    /**
     * 将切面类-目标类集合的映射关系 转化为 目标类-切面对象列表的映射关系
     */
    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> aspectMap) throws Exception {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<>();
        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : aspectMap.entrySet()) {
            // 切面类
            Class<?> aspectClass = proxyEntry.getKey();
            // 目标类集合
            Set<Class<?>> targetClassSet = proxyEntry.getValue();
            // 创建目标类-切面对象列表的映射关系
            for (Class<?> targetClass : targetClassSet) {
                // 切面对象
                Proxy aspect = (Proxy) aspectClass.newInstance();
                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(aspect);
                } else {
                    // 切面对象列表
                    List<Proxy> aspectList = new ArrayList<>();
                    aspectList.add(aspect);
                    targetMap.put(targetClass, aspectList);
                }
            }
        }
        return targetMap;
    }
}
