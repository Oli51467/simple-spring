package com.sdu.springframework.service.impl;

import com.sdu.springframework.core.annotation.Service;
import com.sdu.springframework.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public String test() {
        return "Hello";
    }
}
