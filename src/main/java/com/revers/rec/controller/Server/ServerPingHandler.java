package com.revers.rec.controller.Server;

import com.revers.rec.domain.protobuf.MsgProtobuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerPingHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        MsgProtobuf.connection connection = MsgProtobuf.connection.parseFrom(datagramPacket.content().nioBuffer());

        if(connection != null){
            if("ping".equals(connection.getData())){
                log.info("ServerPingHandler收到ping消息");
                System.out.println("ServerPingHandler收到ping消息");
                channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                        "200", CharsetUtil.UTF_8),datagramPacket.sender()));
            }else {
                log.info("ServerPingHandler收到其他消息");
                System.out.println("ServerPingHandler收到其他消息");
                channelHandlerContext.fireChannelRead(datagramPacket);
            }
        }else {
            log.info("ServerPingHandler收到空消息");
            System.out.println("ServerPingHandler收到空消息");
            channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    "400", CharsetUtil.UTF_8),datagramPacket.sender()));
        }

    }
}
