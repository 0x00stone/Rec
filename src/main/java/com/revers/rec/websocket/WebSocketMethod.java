package com.revers.rec.websocket;

import com.revers.rec.Kademlia.Bucket.Bucket;
import com.revers.rec.Kademlia.Bucket.RoutingTable;
import com.revers.rec.Kademlia.Node.Node;
import com.revers.rec.cli.Menu;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.Data;
import com.revers.rec.domain.Friend;
import com.revers.rec.domain.Message;
import com.revers.rec.domain.front.FrontMessage;
import com.revers.rec.net.Client.ClientOperation;
import com.revers.rec.service.friend.FriendService;
import com.revers.rec.service.message.MessageService;
import com.revers.rec.util.BeanContextUtil;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.Result;
import com.revers.rec.util.cypher.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.Session;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

@Slf4j
@Service
public class WebSocketMethod {

    @Autowired
    FriendService friendService;

    @Autowired
    MessageService messageService;

    @Autowired
    RoutingTable routingTable;

    static WebSocketSession session;

    WebSocketMethod(){
        if(friendService == null){
            friendService = BeanContextUtil.getBean(FriendService.class);
        }
        if(messageService == null){
            messageService = BeanContextUtil.getBean(MessageService.class);
        }
        if(routingTable == null){
            routingTable = BeanContextUtil.getBean(RoutingTable.class);
        }
    }

    public void printMessageList(List<Message> messageList){
        for(Message message : messageList){
            String talker = "";
            String content = "";
            String time = "";
            Friend friend = friendService.findFriendByFriendPublicKey(message.getStrTalker());
            if(friend != null){
                talker = "?????? " + friend.getFriendName();
            }else {
                talker = "????????? " + message.getStrTalker().substring(message.getStrTalker().length()-17,message.getStrTalker().length()-7);
            }

            if (message.getIsSender() == 1){
                talker = AccountConfig.getUsername() + " @ " + talker;
            }else {
                talker = talker + " @ " + AccountConfig.getUsername();
            }

            if(message.getIsSender() == 0)
                messageService.readMessage(message.getMessageId());

            try {
                content = AesUtil.decrypt(AccountConfig.getAeskey(), message.getStrContent());
            } catch (Exception e) {
                log.info("??????????????????");
            }

            time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(message.getCreateTime()));

            System.out.println(talker + " (" + time + ") :" + content);
        }
    }

    private String scanContent(){
        System.out.print("????????????????????????");

        Scanner scanner = new Scanner(System.in);
        while (!scanner.hasNextLine()){}
        return scanner.nextLine();
    }

    /**
     * @description ????????????
     * @message Message
     */
    public void sendMessage(FrontMessage message) {
        Friend toFriend = friendService.findFriendByFriendId(message.getTo().get("id"));
        String toPublicKey;

        if (toFriend == null) {
            //?????????
            toPublicKey = messageService.findPublicKeyByPublicKeyId(message.getTo().get("id"));
        } else {
            toPublicKey = toFriend.getFriendPublicKey();
        }
        if(toPublicKey == null){
            log.info("???????????????????????????");
            return;
        }

        String content = message.getMine().get("content");
        new Thread(new Runnable() {

            @Override
            public void run() {
                Result communicate = null;
                try {
                    communicate = ClientOperation.communicate(toPublicKey, content);
                    if (communicate != null && communicate.getFlag() == ConstantUtil.SUCCESS) {
                        if (ConstantUtil.COMMUNICATE_SUCCESS.equals(((Data) communicate.getData()).getData())) {
                            //????????????
                            messageService.saveMessage(content, toPublicKey, true);

                            log.info("?????????");
                        } else {
                            log.info("?????????");
                        }
                    } else {
                        log.info("????????????");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("????????????");
                }
            }
        }).start();

        Menu.printTips();

    }

    /**
     * @description ????????????????????????
     */
    public HashMap<String,String> countUnHandMessage(){
        HashMap<String,String> result = new HashMap<String,String>();
        result.put("type","unHandMessage");
        //result.put("count",String.valueOf(messageService.countMessage()));
        return result;
    }

    /**
     * @description ????????????
     * @param message
     */
    public synchronized static void sendMessage(String message){
        if(session != null) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
                log.info("????????????????????????,????????????:" + message);
            }
        }
    }

}
