package com.sdu.springframework.core.manager;

import com.sdu.springframework.core.util.ClassUtil;

public final class ManagerLoader {

    public static void init() {
        Class<?>[] classList = {
                ClassManager.class,
                BeanManager.class,
                IocManager.class,
                ControllerManager.class
        };
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName());
        }
    }
}
