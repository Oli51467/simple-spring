package com.sdu.springframework.core.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * AspectProxy是一个切面抽象类，实现了Proxy接口，类中定义了切入点判断和各种增强。
 * 当执行 doProxy() 方法时，会先进行切入点判断，再执行前置增强，代理链的下一个doProxyChain()方法，后置增强等
 */
@Slf4j
public abstract class ProxyAspect implements Proxy {

    @Override
    public Object doProxy(ProxyChain proxyChain) {
        Method targetMethod = proxyChain.getTargetMethod();
        Object[] methodParams = proxyChain.getMethodParams();
        Object result = null;
        begin();
        try {
            if (intercept(targetMethod, methodParams)) {
                before(targetMethod, methodParams);
                result = proxyChain.doProxyChain();
                after(targetMethod, methodParams);
            } else {
                result = proxyChain.doProxyChain();
            }
        } catch (Throwable e) {
            error(targetMethod, methodParams, e);
            return result;
        } finally {
            end();
        }
        return result;
    }

    /**
     * 开始增强
     */
    public void begin() {
    }

    /**
     * 切入点判断
     */
    public boolean intercept(Method method, Object[] params) {
        return true;
    }

    /**
     * 前置增强
     */
    public void before(Method method, Object[] params) {
    }

    /**
     * 后置增强
     */
    public void after(Method method, Object[] params) {
    }

    /**
     * 异常增强
     */
    public void error(Method method, Object[] params, Throwable e) {
        log.error("Fail to invoke proxy, method: {}, params: {}",method, params, e);
    }

    /**
     * 最终增强
     */
    public void end() {
    }
}
