package com.revers.rec.service.user;

import com.revers.rec.util.ResultUtil;

import java.security.NoSuchAlgorithmException;

public interface UserService {

    public ResultUtil login(String username, String password);


    public ResultUtil register(String username, String password) throws NoSuchAlgorithmException;
}
