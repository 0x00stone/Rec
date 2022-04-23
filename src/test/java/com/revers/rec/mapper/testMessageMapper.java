package com.revers.rec.mapper;

import com.revers.rec.RecApplication;
import com.revers.rec.domain.Message;
import com.revers.rec.service.message.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecApplication.class)
public class testMessageMapper {
    @Autowired
    private MessageService messageService;

    @Test
    public void testMessageIncrease() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        messageService.saveMessage("test","publicKey",true);
    }
}
