package com.revers.rec.service.net.Server;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerPingHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    //TODO 两个handler之间的通信
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        System.out.println("Ping received");
        log.info("ServerHandler.channelRead0收到消息");
        MsgProtobuf.connection connection = MsgProtobuf.connection.parseFrom(datagramPacket.content().nioBuffer());
        System.out.println(channelHandlerContext.fireChannelRead(connection));

        if(connection != null){
            System.out.println("监听到srcId:" + connection.getSrcId() + "的消息");
            if(connection.getDestId() == AccountConfig.getId() && connection.getDestPublicKey() == AccountConfig.getPublicKey()){

            }
        }
        channelHandlerContext.writeAndFlush(datagramPacket);
    }
}
