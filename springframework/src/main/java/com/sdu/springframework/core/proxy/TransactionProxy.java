package com.sdu.springframework.core.proxy;

import com.sdu.springframework.core.annotation.Transactional;
import com.sdu.springframework.core.manager.DatabaseManager;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * 事务代理
 * 事务代理相比普通代理的差别是, 我们默认所有Service对象都被代理了, 也就是说通过Service的Class对象, 从Bean容器中得到的都是代理对象。
 * 我们在执行代理方法时会判断目标方法上是否存在 @Transactional 注解, 有就加上事务管理, 没有就直接执行TransactionProxy.doProxy().
 */
@Slf4j
public class TransactionProxy implements Proxy {

    @Override
    public Object doProxy(ProxyChain proxyChain) {
        Object result;
        Method method = proxyChain.getTargetMethod();
        //加了@Transactional注解的方法要做事务处理
        if (method.isAnnotationPresent(Transactional.class)) {
            try {
                DatabaseManager.beginTransaction();
                log.debug("begin transaction");
                result = proxyChain.doProxyChain();
                DatabaseManager.commitTransaction();
                log.debug("commit transaction");
            } catch (Exception e) {
                DatabaseManager.rollbackTransaction();
                log.debug("rollback transaction");
                throw e;
            }
        } else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}