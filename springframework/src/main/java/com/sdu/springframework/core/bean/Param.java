package com.sdu.springframework.core.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 请求参数对象，封装Controller方法的参数
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Param {

    private Map<String, Object> paramMap;

    public boolean isEmpty(){
        return paramMap.isEmpty();
    }
}
