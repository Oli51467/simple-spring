package com.sdu.client.controller;

import com.sdu.springframework.common.RequestMethod;
import com.sdu.springframework.core.annotation.Controller;
import com.sdu.springframework.core.annotation.RequestMapping;
import com.sdu.springframework.core.bean.Data;

@Controller
public class UserController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Data getUserList() {
        return new Data("hello");
    }
}
