package com.sdu.springframework.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事务控制，只能加在方法上。
 * 用AOP实现的思路是，前置增强为开启事务，后置增强为提交事务，异常增强为回滚事务
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {
}