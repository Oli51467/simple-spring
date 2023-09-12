package com.sdu.springframework.core.manager;

import com.sdu.springframework.core.util.ReflectUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * BeanManager 在类加载时就会创建一个Bean容器 BEAN_MAP，然后获取到应用中所有bean的Class对象，再通过反射创建bean实例，储存到 BEAN_MAP 中.
 */
public final class BeanManager {

    /**
     * BEAN_MAP相当于一个bean容器, 拥有项目所有bean的实例
     */
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    static {
        // 获取所有bean
        Set<Class<?>> beanClassSet = ClassManager.getBeanClassSet();
        // 将bean实例化, 并放入bean容器中
        for (Class<?> bean : beanClassSet) {
            Object instance = ReflectUtil.newInstance(bean);
            BEAN_MAP.put(bean, instance);
        }
    }

    /**
     * Java的泛型是类型擦除的，编译器会在编译时执行类型检查，但在运行时会将泛型类型信息擦除，所以要确保在运行时不会发生类型转换错误。
     * 使用了强制类型转换 (T) BEAN_MAP.get(cls)，需确保 BEAN_MAP 中存储的对象是与传入的 Class 对象相兼容的类型，否则会在运行时抛出 ClassCastException 异常。
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        if (!BEAN_MAP.containsKey(clazz)) {
            throw new RuntimeException("Fail to get bean by class: " + clazz);
        }
        return (T) BEAN_MAP.get(clazz);
    }

    /**
     * 设置 Bean 实例
     */
    public static void setBean(Class<?> cls, Object instance) {
        BEAN_MAP.put(cls, instance);
    }
}
