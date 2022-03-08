package com.revers.rec.service;

import com.revers.rec.domain.user;
import com.revers.rec.mapper.userMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class userServiceImpl implements userService{

    @Autowired
    private userMapper userMapper;

    @Override
    public List<user> queryUserList() {

        return userMapper.queryUserList();
    }
}
