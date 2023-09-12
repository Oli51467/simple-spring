package com.sdu.springframework.core.proxy;

/**
 * 代理接口
 */
public interface Proxy {

    /**
     * 执行链式代理
     * 链式代理可将多个代理通过一条链子串起来, 一个个地去执行, 执行顺序取决于添加到链上的先后顺序
     */
    Object doProxy(ProxyChain proxyChain);
}
