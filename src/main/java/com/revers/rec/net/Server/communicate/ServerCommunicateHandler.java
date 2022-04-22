package com.revers.rec.net.Server.communicate;

import com.alibaba.fastjson.JSON;
import com.revers.rec.Kademlia.Bucket.RoutingTable;
import com.revers.rec.Kademlia.Node.KademliaId;
import com.revers.rec.Kademlia.Node.Node;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.ConnectKey;
import com.revers.rec.domain.Data;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.service.connectKey.ConnectKeyService;
import com.revers.rec.util.BeanContextUtil;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.cypher.AesUtil;
import com.revers.rec.util.cypher.DigestUtil;
import com.revers.rec.util.cypher.RsaUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Slf4j
public class ServerCommunicateHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private ConnectKeyService connectKeyService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HashMap<String, Object> map = (HashMap<String, Object>) msg;
        MsgProtobuf.Connection connection = (MsgProtobuf.Connection) map.get("connection");
        DatagramPacket datagramPacket = (DatagramPacket) map.get("datagramPacket");

        if(connection.getMsgType() == ConstantUtil.MSGTYPE_COMMUNICATE){
            this.connectKeyService = BeanContextUtil.getBean(ConnectKeyService.class);
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
                    System.out.println("收到消息："+context);

                    Data dataResponse = new Data();
                    dataResponse.setData(RsaUtil.publicEncrypt(ConstantUtil.COMMUNICATE_SUCCESS,srcPublicKey));
                    dataResponse.setSignature(RsaUtil.privateEncrypt(ConstantUtil.COMMUNICATE_SUCCESS,AccountConfig.getPrivateKey()));
                    String dataResponseJSON = JSON.toJSONString(dataResponse);
                    System.out.println("发送消息："+dataResponseJSON);
                    System.out.println("aes:"+AES);

                    MsgProtobuf.Connection connectionResponse = MsgProtobuf.Connection.newBuilder()
                            .setData(AesUtil.encrypt(AES,dataResponseJSON))
                            .setMsgType(ConstantUtil.MSGTYPE_COMMUNICATE)
                            .build();

                    ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                            connectionResponse.toByteArray()),datagramPacket.sender()));

                    System.out.println("发送回消息："+ConstantUtil.COMMUNICATE_SUCCESS);

                }else {
                    //收到的消息不是自己的消息
                /*MsgProtobuf.Connection connectionResponse = MsgProtobuf.Connection.newBuilder()
                        .setMsgType(ConstantUtil.MSGTYPE_COMMUNICATE)
                        .setSrcPublicKey(AccountConfig.getPublicKey())
                        .set
                        .build();

                ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                        connectionResponse.toByteArray()),datagramPacket.sender()));*/
                }
            }





           /* String publicKey = (String)ctx.attr(AttributeKey.valueOf("publicKey")).get();
            String AES = RsaUtil.privateDecrypt(connection.getData(), AccountConfig.getPrivateKey());
            ctx.attr(AttributeKey.valueOf("aes")).set(AES);

            this.routingTable = BeanContextUtil.getBean(RoutingTable.class);
            routingTable.insert(new Node(new KademliaId(DigestUtil.Sha1AndSha256(publicKey)),connection.getIpv6(),
                    Integer.valueOf(connection.getPort()),publicKey,AES  ));*/
        }else {
            //super.channelRead(ctx, msg);
            log.info("msgType类型错误");
            ctx.close();
        }
    }
}
