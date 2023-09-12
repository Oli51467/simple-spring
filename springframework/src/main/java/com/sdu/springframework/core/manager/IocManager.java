package com.sdu.springframework.core.manager;

import com.sdu.springframework.core.annotation.Autowired;
import com.sdu.springframework.core.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * 遍历Bean容器中的所有bean, 为所有带 @Autowired 注解的属性注入实例. 这个实例从Bean容器中获取.
 */
public final class IocManager {

    static {
        // 遍历容器中的所有bean
        Map<Class<?>, Object> beanMap = BeanManager.getBeanMap();
        if (null != beanMap && !beanMap.isEmpty()) {
            for (Map.Entry<Class<?>, Object> entry : beanMap.entrySet()) {
                // 获取bean的class和实例
                Class<?> beanClass = entry.getKey();
                Object beanInstance = entry.getValue();
                // 反射获得bean的属性
                Field[] declaredFields = beanClass.getDeclaredFields();
                // 遍历bean的属性
                for (Field field : declaredFields) {
                    // 判断属性是否带有AutoWired注解
                    if (field.isAnnotationPresent(Autowired.class)) {
                        // 获取类型属性
                        Class<?> fieldClass = field.getType();
                        // 如果该类型是接口类型，就获取接口对应的实现类
                        fieldClass = getImplementClass(fieldClass);
                        // 获取Class类对应的实例
                        Object instance = beanMap.get(fieldClass);
                        if (null != beanInstance) {
                            // 设置成员变量的值为实例
                            ReflectUtil.setField(beanInstance, field, instance);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取接口对应的实现类
     */
    public static Class<?> getImplementClass(Class<?> interfaceClass) {
        Class<?> implementClass = interfaceClass;
        Set<Class<?>> superClassSet = ClassManager.getSuperClassSet(interfaceClass);
        if (!superClassSet.isEmpty()) {
            implementClass = superClassSet.stream().findFirst().get();
        }
        return implementClass;
    }
}
