package com.sdu.springframework.core.manager;

import com.sdu.springframework.core.annotation.RequestMapping;
import com.sdu.springframework.core.bean.Handler;
import com.sdu.springframework.core.bean.Request;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 通过 ClassManger 工具类获取到应用中所有Controller的Class对象。
 遍历Controller的所有方法，将所有带 @RequestMapping 注解的方法封装为处理器，将 @RequestMapping 注解里的请求路径和请求方法封装成请求对象，存入 REQUEST_MAP 中
 */
public final class ControllerManager {

    /**
     * REQUEST_MAP为 "请求-处理器" 的映射
     */
    private static final Map<Request, Handler> REQUEST_MAP = new HashMap<>();
    
    static {
        // 遍历所有Controller类
        Set<Class<?>> controllerClassSet = ClassManager.getControllerClassSet();
        if (!controllerClassSet.isEmpty()) {
            for (Class<?> controllerClass : controllerClassSet) {
                // 通过反射获取Controller类中的所有方法
                Method[] methods = controllerClass.getDeclaredMethods();
                for (Method method : methods) {
                    // 判断是否带RequestMapping注解
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        // 请求路径
                        String requestPath = requestMapping.value();
                        // 请求方法
                        String requestMethod = requestMapping.method().name();
                        // 封装请求和处理器
                        Request request = new Request(requestMethod, requestPath);
                        Handler handler = new Handler(controllerClass, method);
                        REQUEST_MAP.put(request, handler);
                    }
                }
            }
        }
    }

    /**
     * 获取 Handler
     */
    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return REQUEST_MAP.get(request);
    }
}
