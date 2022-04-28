package com.revers.rec.service.message;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.ChatHistory;
import com.revers.rec.domain.Friend;
import com.revers.rec.domain.Message;
import com.revers.rec.mapper.MessageMapper;
import com.revers.rec.service.friend.FriendService;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.cypher.AesUtil;
import com.revers.rec.util.cypher.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private FriendService friendService;

    @Override
    public Integer saveMessage(String content, String publicKey, boolean isSender) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        int id = messageMapper.getLastInsertId();
        Message message = new Message();
        message.setStrContent(AesUtil.encrypt(AccountConfig.getAeskey(),content));
        message.setStrTalker(publicKey);
        message.setIsRead(0);
        message.setType(1);
        message.setCreateTime(System.currentTimeMillis());
        message.setUpdateTime(System.currentTimeMillis());
        message.setStrTalkerId(DigestUtil.Sha1AndSha256(publicKey));
        if(isSender) {
            message.setIsSender(1);
        }else {
            message.setIsSender(0);
        }
        messageMapper.createMessage(message,AccountConfig.getId());
        int idlast = messageMapper.getLastInsertId();
        return id == idlast ? id : -1;
    }

    @Override
    public void readMessage(Integer messageid) {
        messageMapper.readMessage(messageid,System.currentTimeMillis(),AccountConfig.getId());
    }

    @Override
    public List<Message> getAllMessage() {
        return null;
    }

    @Override
    public List<Message> getRecentMessage() {
        return messageMapper.findRecent(AccountConfig.getId());
    }

    @Override
    public void deleteMessage(String messageid) {
        messageMapper.deleteMessageId(messageid,AccountConfig.getId());
    }

    @Override
    public List<Message> findByTalker(String talker) {
        return messageMapper.findByTalker(talker,AccountConfig.getId());
    }

    @Override
    public List<Message> findByTalker(String talker, int pages) {
        return messageMapper.findByTalkerLimit(talker,AccountConfig.getId(),pages, ConstantUtil.SYSTEM_PAGE);
    }

    @Override
    public List<ChatHistory> chatLog(String id, Integer pages){
        List<ChatHistory> chatHistoryList = new ArrayList<>();
        Friend toFriend = friendService.findFriendByFriendId(id);
        List<Message> byTalker = findByTalker(toFriend.getFriendPublicKey(), pages);
        System.out.println(byTalker.size());
        for(Message message : byTalker){
            ChatHistory chatHistory ;
            try {
                if (message.getIsSender() == 0) {
                    Friend friend = friendService.findFriendByFriendPublicKey(message.getStrTalker());
                    chatHistory = new ChatHistory(String.valueOf(message.getMessageId()),
                            friend.getFriendName(),
                            friend.getFriendId(),
                            friend.getPortrait(),
                            AesUtil.decrypt(AccountConfig.getAeskey(), message.getStrContent()),
                            message.getCreateTime());
                } else {
                    chatHistory = new ChatHistory(String.valueOf(message.getMessageId()),
                            AccountConfig.getUsername(),
                            AccountConfig.getId(),
                            AccountConfig.getPortrait(),
                            AesUtil.decrypt(AccountConfig.getAeskey(), message.getStrContent()),
                            message.getCreateTime());
                }
                chatHistoryList.add(chatHistory);
            }catch (Exception e){
                log.info("AES密钥解密文本失败");
            }

        }
        return chatHistoryList;
    }

    @Override
    public List<Message> findUnreadByTalker(){
        return messageMapper.findUnreadByTalker(AccountConfig.getId());
    }

    @Override
    public Integer countMessage(String id){
        Friend toFriend = friendService.findFriendByFriendId(id);
        return messageMapper.countMessage(AccountConfig.getId(),toFriend.getFriendPublicKey());
    }

    @Override
    public String findPublicKeyByPublicKeyId(String publicKeyId){
        for(Message message : messageMapper.findByTalkerId(publicKeyId,AccountConfig.getId())){
            if(publicKeyId.equals(DigestUtil.Sha1AndSha256(message.getStrTalker())) && publicKeyId.equals(message.getStrTalkerId())){
                if(!AccountConfig.getPublicKey().equals(message.getStrTalker())){
                    return message.getStrTalker();
                }
            }
        }
        return null;
    }

}
