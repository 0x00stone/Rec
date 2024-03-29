package com.revers.rec.net.Server.communicate;

import com.alibaba.fastjson.JSON;
import com.revers.rec.Kademlia.Bucket.RoutingTable;
import com.revers.rec.Kademlia.Node.KademliaId;
import com.revers.rec.Kademlia.Node.Node;
import com.revers.rec.cli.Menu;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.Connect;
import com.revers.rec.domain.ConnectKey;
import com.revers.rec.domain.Data;
import com.revers.rec.domain.Friend;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.net.Client.ClientOperation;
import com.revers.rec.service.connectKey.ConnectKeyService;
import com.revers.rec.service.friend.FriendService;
import com.revers.rec.service.message.MessageService;
import com.revers.rec.service.message.MessageServiceImpl;
import com.revers.rec.util.BeanContextUtil;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.cypher.AesUtil;
import com.revers.rec.util.cypher.DigestUtil;
import com.revers.rec.util.cypher.RsaUtil;
import com.revers.rec.websocket.WebSocketMethod;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

@Slf4j
public class ServerCommunicateHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private MessageService messageService;

    @Autowired
    private ConnectKeyService connectKeyService;

    @Autowired
    private FriendService friendService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HashMap<String, Object> map = (HashMap<String, Object>) msg;
        MsgProtobuf.Connection connection = (MsgProtobuf.Connection) map.get("connection");
        DatagramPacket datagramPacket = (DatagramPacket) map.get("datagramPacket");

        if(connection.getMsgType() == ConstantUtil.MSGTYPE_COMMUNICATE){
            this.connectKeyService = BeanContextUtil.getBean(ConnectKeyService.class);
            this.messageService = BeanContextUtil.getBean(MessageService.class);
            this.friendService = BeanContextUtil.getBean(FriendService.class);
            String srcConnectionPublicKey = connection.getSrcPublicKey();
            String srcId = DigestUtil.Sha1AndSha256(RsaUtil.privateDecrypt(srcConnectionPublicKey,AccountConfig.getPrivateKey()));
            ConnectKey connectKey = connectKeyService.getConnectKey(srcId);
            if(connectKey == null){
                //TODO
                log.info("没有找到对应的connectKey");
            }else {
                String AES = connectKey.getAesKey();

                Data data = JSON.parseObject(AesUtil.decrypt(AES, connection.getData()), Data.class);

                if(AccountConfig.getPublicKey().equals(data.getDestPublicKey())){
                    //收到的消息是自己的消息
                    //String srcPublicKeyDe = RsaUtil.privateDecrypt(data.getSrcPublicKey(),AccountConfig.getPrivateKey());
                    String context = RsaUtil.privateDecrypt(data.getData(),AccountConfig.getPrivateKey());
                    String srcPublicKey = RsaUtil.privateDecrypt(data.getSrcPublicKey(),AccountConfig.getPrivateKey());

                    //显示消息
                    Friend friendByFriendPublicKey = friendService.findFriendByFriendPublicKey(srcPublicKey);
                    if(friendByFriendPublicKey == null){
                        System.out.println("收到消息: "+ context+" 来自陌生人 " + srcPublicKey.substring(srcPublicKey.length()-17,srcPublicKey.length()-7));
                    }else{
                        System.out.println("收到消息: "+ context+" 来自好友 " + friendByFriendPublicKey.getFriendName());
                    }
                    Menu.printTips();


                    //存储消息
                    Integer id = messageService.saveMessage(context, srcPublicKey, false);

                    //发送给前端
                    Friend srcFriend = friendService.findFriendByFriendPublicKey(srcPublicKey);
                    HashMap<String, String> response = new HashMap<>();
                    if(srcFriend == null){
                        response.put("username", "陌生人"+srcPublicKey.substring(srcPublicKey.length()-17,srcPublicKey.length()-7));//消息来源用户名
                        response.put("avatar", "http://127.0.0.1:9000/static/image/avatar/avatar10.jpg");//消息来源用户头像
                        response.put("id", DigestUtil.Sha1AndSha256(srcPublicKey));//消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
                        response.put("type", "friend");//聊天窗口来源类型，从发送消息传递的to里面获取
                        response.put("content", context);//消息内容
                        response.put("cid", String.valueOf(id));//消息id，可不传。除非你要对消息进行一些操作（如撤回）
                        response.put("mine", "false");//是否我发送的消息，如果为true，则会显示在右方
                        response.put("fromid", DigestUtil.Sha1AndSha256(srcPublicKey));//消息的发送者id（比如群组中的某个消息发送者），可用于自动解决浏览器多窗口时的一些问题
                        response.put("timestamp", String.valueOf(System.currentTimeMillis()));//服务端时间戳毫秒数。注意：如果你返回的是标准的 unix 时间戳，记得要 *1000
                    }else {
                        response.put("username", srcFriend.getFriendName());//消息来源用户名
                        response.put("avatar", srcFriend.getPortrait());//消息来源用户头像
                        response.put("id", srcFriend.getFriendId());//消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
                        response.put("type", "friend");//聊天窗口来源类型，从发送消息传递的to里面获取
                        response.put("content", context);//消息内容
                        response.put("cid", String.valueOf(id));//消息id，可不传。除非你要对消息进行一些操作（如撤回）
                        response.put("mine", "false");//是否我发送的消息，如果为true，则会显示在右方
                        response.put("fromid", srcFriend.getFriendId());//消息的发送者id（比如群组中的某个消息发送者），可用于自动解决浏览器多窗口时的一些问题
                        response.put("timestamp", String.valueOf(System.currentTimeMillis()));//服务端时间戳毫秒数。注意：如果你返回的是标准的 unix 时间戳，记得要 *1000
                        //121220461391900
                        //16511579298
                        //1651157929835
                        //1651158138412
                    }
                    WebSocketMethod.sendMessage(JSON.toJSONString(response));

                    //发送回复
                    Data dataResponse = new Data();
                    dataResponse.setDestPublicKey(srcPublicKey);
                    dataResponse.setSrcPublicKey(RsaUtil.publicEncrypt(AccountConfig.getPublicKey(),srcPublicKey));
                    dataResponse.setData(RsaUtil.publicEncrypt(ConstantUtil.COMMUNICATE_SUCCESS,srcPublicKey));
                    dataResponse.setSignature(RsaUtil.privateEncrypt(ConstantUtil.COMMUNICATE_SUCCESS,AccountConfig.getPrivateKey()));
                    String dataResponseJSON = JSON.toJSONString(dataResponse);

                    Connect connectResponse = new Connect();
                    connectResponse.setConnectOrder(connection.getOrder() + 1);
                    connectResponse.setConnectMsgType(ConstantUtil.MSGTYPE_COMMUNICATE);
                    connectResponse.setConnectData(AesUtil.encrypt(AES,dataResponseJSON));
                    connectResponse.setConnectTimestamp(System.currentTimeMillis());

                    MsgProtobuf.Connection connectionResponse = MsgProtobuf.Connection.newBuilder()
                            .setOrder(connectResponse.getConnectOrder())
                            .setData(connectResponse.getConnectData())
                            .setMsgType(connectResponse.getConnectMsgType())
                            .setTimestamp(connectResponse.getConnectTimestamp())
                            .setSignature(connectResponse.getSignature())
                            .build();

                    DatagramPacket datagramPacketResponse = new DatagramPacket(Unpooled.copiedBuffer(
                            connectionResponse.toByteArray()), datagramPacket.sender());
                    ctx.writeAndFlush(datagramPacketResponse);
                    log.info(datagramPacketResponse.toString());

                }else {
                    //收到的消息不是自己的消息
                    data.getDestPublicKey();


                    Data dataResponse = ClientOperation.communicate(data);

                    String dataResponseJSON = JSON.toJSONString(dataResponse);

                    Connect connectResponse = new Connect();
                    connectResponse.setConnectOrder(connection.getOrder() + 1);
                    connectResponse.setConnectMsgType(ConstantUtil.MSGTYPE_COMMUNICATE);
                    connectResponse.setConnectData(AesUtil.encrypt(AES,dataResponseJSON));
                    connectResponse.setConnectTimestamp(System.currentTimeMillis());

                    MsgProtobuf.Connection connectionResponse = MsgProtobuf.Connection.newBuilder()
                            .setData(connectResponse.getConnectData())
                            .setMsgType(connectResponse.getConnectMsgType())
                            .setOrder(connectResponse.getConnectOrder())
                            .setTimestamp(connectResponse.getConnectTimestamp())
                            .setSignature(connectResponse.getSignature())
                            .build();

                    DatagramPacket datagramPacketResponse = new DatagramPacket(Unpooled.copiedBuffer(
                            connectionResponse.toByteArray()), datagramPacket.sender());
                    log.info(datagramPacketResponse.toString());
                    ctx.writeAndFlush(datagramPacketResponse);

                }
            }
        }else {
            //super.channelRead(ctx, msg);
            log.info("msgType类型错误");
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("发送时产生异常");
        super.exceptionCaught(ctx, cause);
    }
}
