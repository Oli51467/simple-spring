package com.sdu.springframework.core.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;

/**
 * 封装 Controller 信息，映射一个request触发哪个Controller的哪个方法
 */
@AllArgsConstructor
@Getter
public class Handler {

    /**
     * Controller 类
     */
    private Class<?> controllerClass;

    /**
     * Controller 方法
     */
    private Method controllerMethod;
}
