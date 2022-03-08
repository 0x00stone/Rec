package com.revers.rec.test;

import com.revers.rec.mapper.userMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class test {
    @Autowired
    private com.revers.rec.mapper.userMapper userMapper;

    @Test
    public void testMapper(){
        System.out.println(userMapper.queryUserList().get(0).toString());
    }
}
