package com.revers.rec.service.user;

import com.revers.rec.util.Result;

import java.security.NoSuchAlgorithmException;

public interface UserService {

    public Result login(String username, String password);


    public Result register(String username, String password) throws NoSuchAlgorithmException;

    public void setSign(String sign);

}
