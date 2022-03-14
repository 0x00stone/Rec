package com.revers.rec.controller;

import com.revers.rec.RecApplication;
import com.revers.rec.config.accountConfig;
import com.revers.rec.domain.user;
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
        userService.register("小小1","123456");

        userService.login("小小1","123456");

        System.out.println(accountConfig.getAeskey());
    }


    @Test
    public void testSelectAllUser(){
        for(com.revers.rec.domain.user user :userMapper.selectAllUser()){
            System.out.println(user);
        }
    }

    @Test
    public void testDeleteAllUser(){
        userMapper.deleteUser("小小");
    }

}