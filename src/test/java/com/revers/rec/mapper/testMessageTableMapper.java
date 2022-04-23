package com.revers.rec.mapper;

import com.revers.rec.RecApplication;
import com.revers.rec.util.BeanContextUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecApplication.class)
public class testMessageTableMapper {
    @Autowired
    MessageTableMapper messageTableMapper;

    @Test
    public void testCreate(){
        messageTableMapper.createTable("test");
    }
}
