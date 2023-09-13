package com.sdu.springframework.core;

import com.sdu.springframework.controller.UserController;
import com.sdu.springframework.core.manager.BeanManager;
import com.sdu.springframework.core.manager.ClassManager;
import com.sdu.springframework.core.manager.ManagerLoader;

import java.util.Set;

public class Main {

    public static void main(String[] args) {
        ManagerLoader.init();
        Set<Class<?>> classSet = ClassManager.getBeanClassSet();
        System.out.println(classSet);
        for (Class<?> aClass : classSet) {
            System.out.println(aClass);
        }
        UserController bean1 = BeanManager.getBean(UserController.class);
        System.out.println(bean1.getUserList());
    }
}
