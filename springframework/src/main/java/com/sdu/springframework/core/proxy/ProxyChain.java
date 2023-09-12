package com.sdu.springframework.core.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理链类, proxyList 存储的是代理列表(也就是增强列表), 当执行doProxyChain() 方法时会按照顺序执行增强, 最后再执行目标方法
 */
public class ProxyChain {

    /**
     * 目标类
     */
    private final Class<?> targetClass;

    /**
     * 目标对象
     */
    private final Object targetObject;

    /**
     * 目标方法
     */
    private final Method targetMethod;

    /**
     * 方法代理
     */
    private final MethodProxy methodProxy;

    /**
     * 方法参数
     */
    private final Object[] methodParams;

    /**
     * 代理列表
     */
    private final List<Proxy> proxyList;

    /**
     * 代理索引
     */
    private int proxyIndex;

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    /**
     * 递归执行代理方法
     */
    public Object doProxyChain() {
        Object methodResult;
        if (proxyIndex < proxyList.size()) {
            // 继续向下执行增强方法
            methodResult = proxyList.get(proxyIndex ++).doProxy(this);
        } else {
            // 目标方法最后执行且只执行一次
            try {
                methodResult = methodProxy.invokeSuper(targetObject, methodParams);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return methodResult;
    }
}
