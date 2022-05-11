package com.revers.rec.cli;

import com.revers.rec.Kademlia.Bucket.Bucket;
import com.revers.rec.Kademlia.Bucket.RoutingTable;
import com.revers.rec.Kademlia.Node.Node;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.Data;
import com.revers.rec.domain.Friend;
import com.revers.rec.domain.Message;
import com.revers.rec.net.Client.ClientOperation;
import com.revers.rec.service.friend.FriendService;
import com.revers.rec.service.message.MessageService;
import com.revers.rec.util.BeanContextUtil;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.Result;
import com.revers.rec.util.cypher.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class ScanThreadMethod {
    @Autowired
    FriendService friendService;

    @Autowired
    MessageService messageService;

    @Autowired
    RoutingTable routingTable;

    ScanThreadMethod(){
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

    public void sf(String friendName)  {
        Friend friend = friendService.findFriendByName(friendName);
        if(friend != null){
            String friendPublicKey = friend.getFriendPublicKey();
            String content = scanContent();
            sendMessage(friendPublicKey, content);
        }else{
            log.info("没有找到好友");
        }
    }

    public void sa(String friendPublicKey) {
        String content = scanContent();
        sendMessage(friendPublicKey, content);
    }

    public void rn(){
        printMessageList(messageService.findUnreadByTalker());
    }


    public void ff(String friendName){
        String friendPublicKey = friendService.findFriendByName(friendName).getFriendPublicKey();
        if(friendPublicKey != null) {
            printMessageList(messageService.findByTalker(friendPublicKey));
        }else{
            log.info("没有找到好友");
        }
    }

    public void fa(String friendPublicKey){
        printMessageList(messageService.findByTalker(friendPublicKey));
    }

    public void b(){
        for(Bucket bucket : routingTable.getBuckets()){
            if(bucket.getDepth() > 0){
                System.out.println(bucket.toString());
            }
        }
    }

    public void t(){
        for(Node node : routingTable.getAllNodes()){
            System.out.println("节点名 : " + node.getNodeId() + " 节点地址 : " + node.getInetAddress() + ":" + node.getPort());
        }
    }

    public void l(String ip,String port){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(ClientOperation.handShake(ip, Integer.valueOf(port)).getFlag() == ConstantUtil.SUCCESS){
                        log.info("握手成功");
                    }else {
                        log.info("握手失败");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    log.info("连接失败");
                }
            }
        }).start();
    }

    public void r(){
        printMessageList(messageService.getRecentMessage());
    }

    public void m(String[] choice){
        friendService.addFriend(choice[1],choice[2]);
    }

    public void u(){
        for(Friend friend : friendService.findAllFriend()){
            System.out.println("好友名称 : " + friend.getFriendName() + " 好友创建日期 : " + new SimpleDateFormat("yyyy年MM月dd日").format(new Date(friend.getCreateTime())) +" 好友公钥 : " + friend.getFriendPublicKey());
        }
    }

    public void h(){
        Menu.printMenu();
    }

    public void printMessageList(List<Message> messageList){
        for(Message message : messageList){
            String talker = "";
            String content = "";
            String time = "";
            Friend friend = friendService.findFriendByFriendPublicKey(message.getStrTalker());
            if(friend != null){
                talker = "好友 " + friend.getFriendName();
            }else {
                talker = "陌生人 " + message.getStrTalker().substring(message.getStrTalker().length()-17,message.getStrTalker().length()-7);
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
                log.info("消息解密失败");
            }

            time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(message.getCreateTime()));

            System.out.println(talker + " (" + time + ") :" + content);
        }
    }

    private String scanContent(){
        System.out.print("请输入消息内容：");

        Scanner scanner = new Scanner(System.in);
        while (!scanner.hasNextLine()){}
        return scanner.nextLine();
    }

    private void sendMessage(String toPublicKey,String content){
        new Thread(new Runnable() {

            @Override
            public void run() {
                Result communicate = null;
                try {
                    communicate = ClientOperation.communicate(toPublicKey, content);
                    if(communicate != null && communicate.getFlag() == ConstantUtil.SUCCESS){
                        if(ConstantUtil.COMMUNICATE_SUCCESS.equals(((Data)communicate.getData()).getData())){
                            //存储消息
                            messageService.saveMessage(content,toPublicKey,true);

                            log.info("已接收");
                        }else {
                            log.info("未接收");
                        }
                    }else {
                        log.info("发送失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("发送失败");
                }
            }
        }).start();
        Menu.printTips();
    }

}
