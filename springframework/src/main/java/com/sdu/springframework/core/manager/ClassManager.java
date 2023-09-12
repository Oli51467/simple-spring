package com.sdu.springframework.core.manager;

import com.sdu.springframework.core.annotation.Controller;
import com.sdu.springframework.core.annotation.Service;
import com.sdu.springframework.core.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * 在自身被加载的时候应用的基础包名，然后通过 ClassUtil 工具类来获取基础包名下所有类，存储到 CLASS_SET 集合中
 */
public final class ClassManager {

    private static final Set<Class<?>> CLASS_SET;

    static {
        // 基础包名
        String basePackages = ConfigManager.getAppBasePackage();
        // 获取包名下的所有类
        CLASS_SET = ClassUtil.getClasses(basePackages);
    }

    /**
     * 获取基础包名下所有 Service 类
     */
    public static Set<Class<?>> getServiceClassSet() {
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz : CLASS_SET) {
            if (clazz.isAnnotationPresent(Service.class)) {
                classSet.add(clazz);
            }
        }
        return classSet;
    }

    /**
     * 获取基础包名下所有 Controller 类
     */
    public static Set<Class<?>> getControllerClassSet() {
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz : CLASS_SET) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                classSet.add(clazz);
            }
        }
        return classSet;
    }

    /**
     * 获取基础包名下所有 Bean 类（包括：Controller、Service）
     */
    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> classSet = new HashSet<>();
        classSet.addAll(getServiceClassSet());
        classSet.addAll(getControllerClassSet());
        return classSet;
    }

    /**
     * 获取基础包名下某父类的所有子类 或某接口的所有实现类
     */
    public static Set<Class<?>> getSuperClassSet(Class<?> superClass) {
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz : CLASS_SET) {
            // isAssignableFrom() 表示superClass 和 cls 是否相同或 superClass 是否是 cls 的父类/接口
            if (superClass.isAssignableFrom(clazz) && !superClass.equals(clazz)) {
                classSet.add(clazz);
            }
        }
        return classSet;
    }

    /**
     * 获取基础包名下带有某注解的所有类
     */
    public static Set<Class<?>> getAnnotationClassSet(Class<? extends Annotation> annotation) {
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz : CLASS_SET) {
            if (clazz.isAnnotationPresent(annotation)) {
                classSet.add(clazz);
            }
        }
        return classSet;
    }
}
