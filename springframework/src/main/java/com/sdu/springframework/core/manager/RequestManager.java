package com.sdu.springframework.core.manager;

import com.sdu.springframework.core.bean.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 前端控制器接收到HTTP请求后，从HTTP中获取请求参数，然后封装到Param对象中
 */
public class RequestManager {

    /**
     * 获取请求参数
     */
    public static Param createParam(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<>();
        // 拿到所有key
        Enumeration<String> parameterNames = request.getParameterNames();
        // 没有参数
        if (!parameterNames.hasMoreElements()) {
            return null;
        }
        // 遍历key，从request中获取value
        while (parameterNames.hasMoreElements()) {
            String filedName = parameterNames.nextElement();
            String fieldValue = request.getParameter(filedName);
            paramMap.put(filedName, fieldValue);
        }
        return new Param(paramMap);
    }
}
