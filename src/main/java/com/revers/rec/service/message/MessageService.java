package com.revers.rec.service.message;

import com.revers.rec.domain.ChatHistory;
import com.revers.rec.domain.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface MessageService {

    public Integer saveMessage(String content, String publicKey, boolean isSender) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;

    public void readMessage(Integer messageid);

    public List<Message> getAllMessage();

    public List<Message> getRecentMessage();

    public void deleteMessage(String messageid);

    List<Message> findByTalker(String talker);

    List<Message> findByTalker(String talker, int pages);

    List<ChatHistory> chatLog(String id, Integer pages);

    List<Message> findUnreadByTalker();

    Integer countMessage(String id);

    String findPublicKeyByPublicKeyId(String publicKeyId);


 /*private Integer messageId;
    private Integer type;
    private Long createTime;
    private Integer isSender;
    private Integer isRead;
    private String strTalker;
    private String strContent;*/
}
