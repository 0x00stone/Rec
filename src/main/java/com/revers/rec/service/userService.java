package com.revers.rec.service;

import com.revers.rec.domain.user;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface userService {

    //0 登录成功 , 1 登录失败,用户不存在
    public int login(String username,String password);


    //0 创建成功 , 1 创建失败,用户已存在
    public int register(String username,String password) throws NoSuchAlgorithmException;
}
