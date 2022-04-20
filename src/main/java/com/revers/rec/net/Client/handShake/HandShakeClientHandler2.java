package com.revers.rec.net.Client.handShake;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.cypher.AesUtil;
import com.revers.rec.util.cypher.RsaUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;

import java.util.HashMap;

/**
 * @author Revers.
 * @date 2022/04/19 23:11
 **/
public class HandShakeClientHandler2 extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        MsgProtobuf.Connection connection = MsgProtobuf.Connection.parseFrom(datagramPacket.content().nioBuffer());
        HashMap<String,Object> map = new HashMap<>();
        map.put("connection",connection);
        map.put("datagramPacket",datagramPacket);
        if(connection == null){
            return;
        }
        if(connection.getMsgType() == ConstantUtil.MSGTYPE_HANDSHAKE_2){
            channelHandlerContext.attr(AttributeKey.valueOf("publicKey")).set(connection.getData());
            String AES = AesUtil.getAseKey(256);
            channelHandlerContext.attr(AttributeKey.valueOf("aes")).set(AES);

            MsgProtobuf.Connection connectionResponse = MsgProtobuf.Connection.newBuilder()
                    .setOrder(connection.getOrder()+1)
                    .setMsgType(ConstantUtil.MSGTYPE_HANDSHAKE_3)
                    .setData(RsaUtil.publicEncrypt(AES,connection.getData()))
                    .setIpv6(AccountConfig.getIpv6())
                    .setPort(String.valueOf(AccountConfig.getIpv6Port()))
                    .build();

            channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    connectionResponse.toByteArray()),datagramPacket.sender()));
        }else {
            channelHandlerContext.fireChannelRead(map);
        }
    }
}
