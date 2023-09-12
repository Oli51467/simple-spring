package com.sdu.springframework.core.bean;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装Controller方法的视图返回结果.
 */
@Getter
public class View {

    /**
     * 视图路径
     */
    private final String path;

    /**
     * 模型数据
     */
    private final Map<String, Object> model;

    public View(String path) {
        this.path = path;
        model = new HashMap<>();
    }

    public View addModel(String key, Object value) {
        model.put(key, value);
        return this;
    }
}
