package com.sdu.springframework.core.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 封装Controller方法的JSON返回结果
 */
@AllArgsConstructor
@Getter
public class Data {

    /**
     * 模型数据
     */
    private Object model;
}

