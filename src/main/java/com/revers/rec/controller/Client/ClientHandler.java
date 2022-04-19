package com.revers.rec.controller.Client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

public class ClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {

        System.out.println("Received: " + datagramPacket.content().toString(CharsetUtil.UTF_8));
        channelHandlerContext.attr(AttributeKey.valueOf("result")).set(datagramPacket.content().toString(CharsetUtil.UTF_8));
        channelHandlerContext.close();
    }
}
