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
import com.revers.rec.util.ResultUtil;
import com.revers.rec.util.cypher.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * 监听输入
 *
 * @author Revers.
 * @date 2022/04/22 23:09
 **/

@Slf4j
public class ScanThread implements Runnable{
    @Autowired
    FriendService friendService;

    @Autowired
    MessageService messageService;

    @Autowired
    RoutingTable routingTable;

//TODO 实现
    public ScanThread() {
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

    @Override
    public void run() {

        while (true) {

            String[] choice = new Scanner(System.in).nextLine().split(" ");
            if("/m".equals(choice[0])){
                log.info("/m");
                if(choice.length != 3){
                    log.info("参数错误");
                    continue;
                }else {
                    m(choice);
                    continue;
                }
            }
            if("/r".equals(choice[0])){
                r();
                continue;
            }

            if("/u".equals(choice[0])){
                u();
                continue;
            }

            if("/l".equals(choice[0])){
                if(choice.length == 3) {
                    l(choice[1], choice[2]);
                    continue;
                }else {
                    log.info("参数错误");
                }
            }

            if("/b".equals(choice[0])){
                b();
                continue;
            }

            if("/t".equals(choice[0])){
                t();
                continue;
            }

            if("/ff".equals(choice[0])){
                if(choice.length == 2){
                    ff(choice[1]);
                }else {
                    log.info("参数错误");
                }
                continue;
            }
            if("/fa".equals(choice[0])){
                if(choice.length == 2){
                    fa(choice[1]);
                }else {
                    log.info("参数错误");
                }
                continue;
            }

            if("/h".equals(choice[0])){
                h();
                continue;
            }

            if("/rn".equals(choice[0])){
                rn();
                continue;
            }

            if("/sf".equals(choice[0])){
                sf(choice[1]);
                continue;
            }

            if("/sa".equals(choice[0])){
                sa(choice[1]);
                continue;
            }

        }
    }

    private void sf(String friendName){
        Friend friend = friendService.findFriendByName(friendName);
        if(friend != null){
            String friendPublicKey = friend.getFriendPublicKey();
            sendMessage(friendPublicKey);
        }else{
            log.info("没有找到好友");
        }
        Menu.printTips();
    }

    private void sa(String friendPublicKey){
        sendMessage(friendPublicKey);
        Menu.printTips();
    }

    private void rn(){
        printMessageList(messageService.findUnreadByTalker());
        Menu.printTips();
    }


    private void ff(String friendName){
        String friendPublicKey = friendService.findFriendByName(friendName).getFriendPublicKey();
        printMessageList(messageService.findByTalker(friendPublicKey));
        Menu.printTips();
    }

    private void fa(String friendPublicKey){

        printMessageList(messageService.findByTalker(friendPublicKey));
        Menu.printTips();
    }

    private void b(){
        for(Bucket bucket : routingTable.getBuckets()){
            if(bucket.getDepth() > 0){
                System.out.println(bucket.toString());
            }
        }
        Menu.printTips();
    }

    private void t(){
        for(Node node : routingTable.getAllNodes()){
            System.out.println("节点名 : " + node.getNodeId() + " 节点地址 : " + node.getInetAddress() + ":" + node.getPort());
        }
        Menu.printTips();
    }

    private void l(String ip,String port){
        try {
            if(ClientOperation.handShake(ip, Integer.valueOf(port)).getFlag()){
                log.info("握手成功");
            }else {
                log.info("握手失败");
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.info("连接失败");
        }
        Menu.printTips();
    }

    private void r(){
        printMessageList(messageService.getRecentMessage());
        Menu.printTips();
    }

    private void m(String[] choice){
        friendService.addFriend(choice[1],choice[2]);
        Menu.printTips();
    }

    private void u(){
        for(Friend friend : friendService.findAllFriend()){
            System.out.println("好友名称 : " + friend.getFriendName() + " 好友创建日期 : " + new SimpleDateFormat("yyyy年MM月dd日").format(new Date(friend.getCreateTime())) +" 好友公钥 : " + friend.getFriendPublicKey());
        }
        Menu.printTips();
    }

    private void h(){

        Menu.printMenu();
    }

    private void printMessageList(List<Message> messageList){
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

    private void sendMessage(String toPublicKey){
        System.out.print("请输入消息内容：");
        String content = "";

        Scanner scanner = new Scanner(System.in);
        while (!scanner.hasNextLine()){
            content = scanner.nextLine();
        }

        ResultUtil communicate = null;
        try {
            communicate = ClientOperation.communicate(toPublicKey, content);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(communicate != null){
                if(ConstantUtil.COMMUNICATE_SUCCESS.equals(((Data)communicate.getData()).getData())){
                    log.info("已接收");
                }else {
                    log.info("未接收");
                }
            }else {
                log.info("发送失败");
            }
        }
    }
}
