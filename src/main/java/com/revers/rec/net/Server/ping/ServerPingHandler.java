package com.revers.rec.net.Server.ping;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.domain.protobuf.MsgProtobuf.Connection;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.cypher.Md5;
import com.revers.rec.util.cypher.Rsa;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Random;

/**  处理ping消息 **/
@Slf4j
public class ServerPingHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        Connection connection = Connection.parseFrom(datagramPacket.content().nioBuffer());
        HashMap<String,Object> map = new HashMap<>();
        map.put("connection",connection);
        map.put("datagramPacket",datagramPacket);
        if(connection == null){
            return;
        }
        if(connection.getMsgType() == ConstantUtil.MSGTYPE_PING_REQUEST){
            Connection connectionResponse ;

            if("ping".equals(connection.getData())){
                connectionResponse = MsgProtobuf.Connection.newBuilder()
                        .setOrder(connection.getOrder()+1)
                        .setMsgType(ConstantUtil.MSGTYPE_PING_RESPONSE)
                        .setData(ConstantUtil.PING_SUCCESS)
                        .build();
            }else {
                connectionResponse = MsgProtobuf.Connection.newBuilder()
                        .setOrder(connection.getOrder()+1)
                        .setMsgType(ConstantUtil.MSGTYPE_PING_RESPONSE)
                        .setData(ConstantUtil.PING_FAILURE)
                        .build();
            }

            log.info("接收到来自 " + datagramPacket.sender() + " 的ping消息");
            channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    connectionResponse.toByteArray()),datagramPacket.sender()));
        }else {
            channelHandlerContext.fireChannelRead(map);
        }
    }
}
