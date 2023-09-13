package com.sdu.springframework.core.aspect;

import com.sdu.springframework.core.annotation.Aspect;
import com.sdu.springframework.core.proxy.ProxyAspect;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * 性能切面, 获取接口执行时间
 */
@Aspect(pkg = "com.sdu.springframework.controller", clazz = "UserController")
@Slf4j
public class EfficientAspect extends ProxyAspect {

    private long begin;

    /**
     * 切入点判断
     */
    @Override
    public boolean intercept(Method method, Object[] params) {
        return method.getName().equals("getUserList");
    }

    @Override
    public void before(Method method, Object[] params) {
        log.debug("---------- begin ----------");
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Method method, Object[] params) {
        log.debug(String.format("time: %dms", System.currentTimeMillis() - begin));
        log.debug("----------- end -----------");
    }
}
