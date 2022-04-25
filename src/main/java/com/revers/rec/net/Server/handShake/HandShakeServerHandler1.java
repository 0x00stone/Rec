package com.revers.rec.net.Server.handShake;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.Connect;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.util.ConstantUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @author Revers.
 * @date 2022/04/19 23:49
 **/
@Slf4j
public class HandShakeServerHandler1 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HashMap<String, Object> map = (HashMap<String, Object>) msg;
        MsgProtobuf.Connection connection = (MsgProtobuf.Connection) map.get("connection");
        DatagramPacket datagramPacket = (DatagramPacket) map.get("datagramPacket");

        if (connection.getMsgType() == ConstantUtil.MSGTYPE_HANDSHAKE_1) {
            ctx.attr(AttributeKey.valueOf("publicKey")).set(connection.getData());

            Connect connectResponse = new Connect();
            connectResponse.setConnectOrder(connection.getOrder() + 1);
            connectResponse.setConnectMsgType(ConstantUtil.MSGTYPE_HANDSHAKE_2);
            connectResponse.setConnectData(AccountConfig.getPublicKey());
            connectResponse.setConnectTimestamp(System.currentTimeMillis());

            MsgProtobuf.Connection connectionResponse = MsgProtobuf.Connection.newBuilder()
                    .setOrder(connectResponse.getConnectOrder())
                    .setMsgType(connectResponse.getConnectMsgType())
                    .setTimestamp(connectResponse.getConnectTimestamp())
                    .setData(connectResponse.getConnectData())
                    .setSignature(connectResponse.getSignature())
                    .build();

            DatagramPacket datagramPacketResponse = new DatagramPacket(Unpooled.copiedBuffer(
                    connectionResponse.toByteArray()), datagramPacket.sender());
            ctx.writeAndFlush(datagramPacketResponse);
            log.info(datagramPacketResponse.toString());
        } else {
            super.channelRead(ctx, msg);
        }

    }
}
