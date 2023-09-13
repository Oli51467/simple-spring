package com.sdu.springframework.controller;

import com.sdu.springframework.common.RequestMethod;
import com.sdu.springframework.core.annotation.Autowired;
import com.sdu.springframework.core.annotation.Controller;
import com.sdu.springframework.core.annotation.RequestMapping;
import com.sdu.springframework.core.bean.Data;
import com.sdu.springframework.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Data getUserList() {
        return new Data(userService.test());
    }
}
