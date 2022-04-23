package com.revers.rec.service.message;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.Message;
import com.revers.rec.mapper.MessageMapper;
import com.revers.rec.util.BeanContextUtil;
import com.revers.rec.util.cypher.AesUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void saveMessage(String content, String publicKey,boolean isSender) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Message message = new Message();
        message.setStrContent(AesUtil.encrypt(AccountConfig.getAeskey(),content));
        message.setStrTalker(publicKey);
        message.setIsRead(0);
        message.setType(1);
        message.setCreateTime(System.currentTimeMillis());
        message.setUpdateTime(System.currentTimeMillis());
        if(isSender) {
            message.setIsSender(1);
        }else {
            message.setIsSender(0);
        }
        messageMapper.createMessage(message,AccountConfig.getUsername());
    }

    @Override
    public void readMessage(Integer messageid) {
        messageMapper.readMessage(messageid,System.currentTimeMillis());
    }

    @Override
    public List<Message> getAllMessage() {
        return null;
    }

    @Override
    public List<Message> getRecentMessage() {
        return null;
    }

    @Override
    public void deleteMessage(String messageid) {

    }
}
