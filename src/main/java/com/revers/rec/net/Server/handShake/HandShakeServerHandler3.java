package com.revers.rec.net.Server.handShake;

import com.revers.rec.Kademlia.Bucket.RoutingTable;
import com.revers.rec.Kademlia.Node.KademliaId;
import com.revers.rec.Kademlia.Node.Node;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.util.BeanContextUtil;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.cypher.AesUtil;
import com.revers.rec.util.cypher.DigestUtil;
import com.revers.rec.util.cypher.RsaUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * @author Revers.
 * @date 2022/04/19 23:49
 **/
public class HandShakeServerHandler3 extends ChannelInboundHandlerAdapter {
    @Autowired
    private RoutingTable routingTable;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HashMap<String, Object> map = (HashMap<String, Object>) msg;
        MsgProtobuf.Connection connection = (MsgProtobuf.Connection) map.get("connection");
        DatagramPacket datagramPacket = (DatagramPacket) map.get("datagramPacket");
        if(connection.getMsgType() == ConstantUtil.MSGTYPE_HANDSHAKE_3){
            String publicKey = (String)ctx.attr(AttributeKey.valueOf("publicKey")).get();
            String AES = RsaUtil.privateDecrypt(connection.getData(),AccountConfig.getPrivateKey());
            ctx.attr(AttributeKey.valueOf("aes")).set(AES);

            this.routingTable = BeanContextUtil.getBean(RoutingTable.class);
            routingTable.insert(new Node(new KademliaId(DigestUtil.Sha1AndSha256(publicKey)),connection.getIpv6(),
                    Integer.valueOf(connection.getPort()),publicKey,AES  ));

            MsgProtobuf.Connection connectionResponse = MsgProtobuf.Connection.newBuilder()
                    .setOrder(connection.getOrder()+1)
                    .setMsgType(ConstantUtil.MSGTYPE_HANDSHAKE_4)
                    .setData(AesUtil.encrypt(AES,"success"))
                    .setIpv6(AccountConfig.getIpv6())
                    .setPort(String.valueOf(AccountConfig.getIpv6Port()))
                    .build();

            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    connectionResponse.toByteArray()),datagramPacket.sender()));
        }else {
            super.channelRead(ctx, msg);
        }
    }
}
