package com.revers.rec.controller.Server;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.protobuf.MsgProtobuf.Connection;
import com.revers.rec.service.net.Session;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.cypher.DigestUtil;
import com.revers.rec.util.cypher.Rsa;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

@Slf4j
public class ServerHandShakeType1Handler extends ChannelInboundHandlerAdapter {
    @Autowired
    Session session;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        HashMap<String, Object> map = (HashMap<String, Object>) msg;
        Connection connection = (Connection) map.get("connection");
        DatagramPacket datagramPacket = (DatagramPacket) map.get("datagramPacket");

        if (connection.getMsgType() == ConstantUtil.MSGTYPE_TYPE1) {
            log.info("ServerHandShakeType1Handler收到type1消息");
            String Aes = Rsa.privateDecrypt(connection.getData(), AccountConfig.getPrivateKey());
            Object[] objects = new Object[4];
            objects[0] = connection.getMsgType();
            objects[1] = connection.getSrcPublicKey();
            objects[2] = connection.getData();
            objects[3] = connection.getOrder();
            session.handShakeSession.put(DigestUtil.Sha1AndSha256(connection.getSrcPublicKey()),objects);

            //TODO 回复msgtype2
            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    ConstantUtil.CONNECTION_TYPE1_SUCCESS, CharsetUtil.UTF_8), datagramPacket.sender()));
        } else {
            log.info("ServerHandShakeType1Handler收到非type1消息");
            ctx.fireChannelRead(map);
        }

    }
}
