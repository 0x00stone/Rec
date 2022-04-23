package com.revers.rec.mapper;

import com.revers.rec.RecApplication;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.Message;
import com.revers.rec.service.message.MessageService;
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
    @Autowired
    MessageService messageService;

    @Test
    public void testCreate(){
        messageTableMapper.createTable("test");
    }

    @Test
    public void testMessage(){
       // AccountConfig.
        AccountConfig.setId("9d06241c8913d9d3389626a415bb0c9327d28602");
        for(Message m : messageService.getRecentMessage()){
            System.out.println(m.toString());
        }
    }
}
