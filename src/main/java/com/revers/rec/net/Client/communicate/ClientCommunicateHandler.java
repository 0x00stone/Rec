package com.revers.rec.net.Client.communicate;

import com.revers.rec.domain.protobuf.MsgProtobuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;

public class ClientCommunicateHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        MsgProtobuf.Connection connection = MsgProtobuf.Connection.parseFrom(datagramPacket.content().nioBuffer());
        channelHandlerContext.attr(AttributeKey.valueOf("data")).set(connection.getData());


    }
}
