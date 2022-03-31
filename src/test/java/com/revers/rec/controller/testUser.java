package com.revers.rec.controller;

import com.revers.rec.RecApplication;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.User;
import com.revers.rec.mapper.UserMapper;
import com.revers.rec.service.UserServiceImpl;
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
    private UserMapper userMapper;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AccountConfig accountConfig;

    @Test
    public void testServiceCreateUser() throws NoSuchAlgorithmException {
        userService.register("小小1","123456");

        userService.login("小小1","123456");

        System.out.println(accountConfig.getAeskey());
    }


    @Test
    public void testSelectAllUser(){
        for(User user :userMapper.selectAllUser()){
            System.out.println(user);
        }
    }

    @Test
    public void testDeleteAllUser(){
        userMapper.deleteUser("小小");
    }

}