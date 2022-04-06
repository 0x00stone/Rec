package com.revers.rec.net.Server;

import com.revers.rec.domain.Connection;
import com.revers.rec.util.Byte;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        // 因为Netty对UDP进行了封装，所以接收到的是DatagramPacket对象。
        ByteBuf content = datagramPacket.content();
        System.out.println(datagramPacket.content());
        System.out.println(datagramPacket.content().toString());
        System.out.println(datagramPacket.content().array());

        System.out.println(datagramPacket.content().array());
        Connection connection = (Connection)Byte.bytesToObject(datagramPacket.content().array());
        System.out.println(connection);

        if(connection != null){
            System.out.println("srcId:" + connection.getSrcId());
        }

        channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    "结果：收到消息",CharsetUtil.UTF_8),datagramPacket.sender()));

    }
}