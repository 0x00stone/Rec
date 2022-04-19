package com.revers.rec.controller.Client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

/**
 * @author Revers.
 * @date 2022/04/19 23:11
 **/
public class HandShakeClientHandler4 extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        System.out.println("HandShakeClientHandler4");
    }
}
