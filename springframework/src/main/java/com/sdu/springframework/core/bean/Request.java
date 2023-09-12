package com.sdu.springframework.core.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 封装请求信息
 */
@AllArgsConstructor
@Getter
public class Request {
    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求路径，对应 @RequestMapping 注解里的方法和路径
     */
    private String requestPath;

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + requestMethod.hashCode();
        result = 31 * result + requestPath.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Request)) return false;
        Request request = (Request) obj;
        return request.getRequestPath().equals(this.requestPath) && request.getRequestMethod().equals(this.requestMethod);
    }
}
