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
import java.util.Random;

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
        Random random  = new Random(System.currentTimeMillis());
        String username = String.valueOf(random.nextDouble(100000));
        String password = String.valueOf(random.nextDouble(100000));


        userService.register(username,password);

        userService.login(username,password);

        System.out.println(accountConfig);

        System.out.println(accountConfig.getAeskey());

        if(username.equals(accountConfig.getUsername())){
            System.out.println("用户创建成功");
        }
    }


    @Test
    public void testSelectAllUser(){
        for(User user :userMapper.findAll()){
            System.out.println(user);
        }
    }

    @Test
    public void testDeleteAllUser(){
        userMapper.deleteUser("小小");
    }

}