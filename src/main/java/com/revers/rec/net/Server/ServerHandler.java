package com.revers.rec.net.Server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        System.err.println(packet);
        System.out.println("I receive your message");
        if ("QOTM?".equals(packet.content().toString(CharsetUtil.UTF_8))) {
            ctx.write(new DatagramPacket(
                    Unpooled.copiedBuffer("QOTM: " + "服务端收到了这条消息", CharsetUtil.UTF_8), packet.sender()));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        // We don't close the channel because we can keep serving requests.
    }
}