package com.revers.rec.net.Server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2016/1/21 0021.
 */
public class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        // 因为Netty对UDP进行了封装，所以接收到的是DatagramPacket对象。
        String req = datagramPacket.content().toString(CharsetUtil.UTF_8);
        System.out.println(req);

            channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    "结果：收到消息",CharsetUtil.UTF_8),datagramPacket.sender()));

    }
}