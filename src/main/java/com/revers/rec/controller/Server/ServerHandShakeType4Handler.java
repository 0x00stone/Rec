package com.revers.rec.controller.Server;

import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.util.ConstantUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class ServerHandShakeType4Handler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HashMap<String,Object> map = (HashMap<String, Object>) msg;
        MsgProtobuf.Connection connection = (MsgProtobuf.Connection)map.get("connection");
        DatagramPacket datagramPacket = (DatagramPacket) map.get("datagramPacket");

        if(connection != null){
            if(connection.getMsgType() == ConstantUtil.MSGTYPE_TYPE4){
                log.info("ServerHandShakeType4Handler收到type4消息");
                ctx.fireChannelRead(map);
            }else {
                log.info("ServerHandShakeType4Handler收到非type4消息");
                ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                        ConstantUtil.TYPE_ILLEGAL, CharsetUtil.UTF_8),datagramPacket.sender()));
            }
        }else {
            log.info("ServerHandShakeType4Handler收到空消息");
            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    ConstantUtil.CONNECTION_GETTING_NONE, CharsetUtil.UTF_8),datagramPacket.sender()));
        }
    }
}
