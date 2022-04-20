package com.revers.rec.net.Client.ping;

import com.revers.rec.domain.protobuf.MsgProtobuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

public class ClientPingHandler extends SimpleChannelInboundHandler<DatagramPacket>{
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        MsgProtobuf.Connection connection = MsgProtobuf.Connection.parseFrom(datagramPacket.content().nioBuffer());
        channelHandlerContext.attr(AttributeKey.valueOf("msgType")).set(connection.getMsgType());
        channelHandlerContext.attr(AttributeKey.valueOf("order")).set(connection.getOrder());
        channelHandlerContext.attr(AttributeKey.valueOf("data")).set(connection.getData());
        channelHandlerContext.close();
    }
}
