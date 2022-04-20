package com.revers.rec.net.Server.handShake;

import com.revers.rec.Kademlia.Bucket.RoutingTable;
import com.revers.rec.Kademlia.Node.KademliaId;
import com.revers.rec.Kademlia.Node.Node;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.HandShake;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.mapper.HandShakeMapper;
import com.revers.rec.service.net.Session;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.cypher.DigestUtil;
import com.revers.rec.util.cypher.Rsa;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * @author Revers.
 * @date 2022/04/19 23:49
 **/
public class HandShakeServerHandler1 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HashMap<String, Object> map = (HashMap<String, Object>) msg;
        MsgProtobuf.Connection connection = (MsgProtobuf.Connection) map.get("connection");
        DatagramPacket datagramPacket = (DatagramPacket) map.get("datagramPacket");

        if(connection.getMsgType() == ConstantUtil.MSGTYPE_HANDSHAKE_1){
            ctx.attr(AttributeKey.valueOf("publicKey")).set(connection.getData());

            MsgProtobuf.Connection connectionResponse = MsgProtobuf.Connection.newBuilder()
                    .setOrder(connection.getOrder()+1)
                    .setMsgType(ConstantUtil.MSGTYPE_HANDSHAKE_2)
                    .setData(AccountConfig.getPublicKey())
                    .build();

            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    connectionResponse.toByteArray()),datagramPacket.sender()));
        }else {
            super.channelRead(ctx, msg);
        }
    }
}
