package com.revers.rec.service;

import com.revers.rec.util.Result;

import java.security.NoSuchAlgorithmException;

public interface UserService {

    public Result login(String username, String password);


    public Result register(String username,String password) throws NoSuchAlgorithmException;
}
