package com.revers.rec.service;

import com.revers.rec.domain.user;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface userService {

    public boolean login(String username,String password);

    public void createUser(String username,String password) throws NoSuchAlgorithmException;
}
