package com.revers.rec.controller;

import com.revers.rec.RecApplication;
import com.revers.rec.config.accountConfig;
import com.revers.rec.mapper.userMapper;
import com.revers.rec.service.userServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecApplication.class)
public class testUser {
    @Autowired
    private userMapper userMapper;

    @Autowired
    private userServiceImpl userService;

    @Autowired
    private accountConfig accountConfig;

    @Test
    public void testServiceCreateUser() throws NoSuchAlgorithmException {
        userService.createUser("小小","123456");

        userService.login("小小","123456");

        System.out.println(accountConfig.getAeskey());
    }


}