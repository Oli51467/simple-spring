package com.sdu.springframework.core.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 */
@Slf4j
public final class ReflectUtil {

    /**
     * 根据一个类创建实例
     * @param clazz 类
     * @return 实例
     */
    public static Object newInstance(Class<?> clazz) {
        Object instance;
        try {
            instance = clazz.newInstance();
        } catch (Exception e) {
            log.error("Fail to new instance", e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 根据类名创建实例
     * @param clazzName 类名
     * @return 实例
     */
    public static Object newInstance(String clazzName) {
        Class<?> clazz = ClassUtil.loadClass(clazzName);
        return newInstance(clazz);
    }

    /**
     * 调用方法
     */
    public static Object invokeMethod(Object obj, Method method, Object... args) {
        Object result;
        try {
            method.setAccessible(true);
            result = method.invoke(obj, args);
        } catch (Exception e) {
            log.error("Fail to invoke method", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 设置成员变量的值
     */
    public static void setField(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true); //去除私有权限
            field.set(obj, value);
        } catch (Exception e) {
            log.error("Fail to set field", e);
            throw new RuntimeException(e);
        }
    }
}
