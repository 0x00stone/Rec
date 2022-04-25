package com.revers.rec.net.Client.handShake;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.Connect;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.cypher.AesUtil;
import com.revers.rec.util.cypher.RsaUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @author Revers.
 * @date 2022/04/19 23:11
 **/
@Slf4j
public class HandShakeClientHandler2 extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HashMap<String, Object> map = (HashMap<String, Object>) msg;
        MsgProtobuf.Connection connection = (MsgProtobuf.Connection) map.get("connection");
        DatagramPacket datagramPacket = (DatagramPacket) map.get("datagramPacket");

        if(connection.getMsgType() == ConstantUtil.MSGTYPE_HANDSHAKE_2){
            ctx.attr(AttributeKey.valueOf("publicKey")).set(connection.getData());
            String AES = AesUtil.getAseKey(256);
            ctx.attr(AttributeKey.valueOf("aes")).set(AES);

            Connect connectResponse = new Connect();
            connectResponse.setConnectOrder(connection.getOrder() + 1);
            connectResponse.setConnectMsgType(ConstantUtil.MSGTYPE_HANDSHAKE_3);
            connectResponse.setConnectData(RsaUtil.publicEncrypt(AES,connection.getData()));
            connectResponse.setConnectIpv6Ip(AccountConfig.getIpv6());
            connectResponse.setConnectIpv6Port(String.valueOf(AccountConfig.getIpv6Port()));
            connectResponse.setConnectTimestamp(System.currentTimeMillis());

            MsgProtobuf.Connection connectionResponse = MsgProtobuf.Connection.newBuilder()
                    .setOrder(connectResponse.getConnectOrder())
                    .setMsgType(connectResponse.getConnectMsgType())
                    .setData(connectResponse.getConnectData())
                    .setIpv6(connectResponse.getConnectIpv6Ip())
                    .setPort(connectResponse.getConnectIpv6Port())
                    .setTimestamp(connectResponse.getConnectTimestamp())
                    .setSignature(connectResponse.getSignature())
                    .build();

            DatagramPacket datagramPacketResponse = new DatagramPacket(Unpooled.copiedBuffer(
                    connectionResponse.toByteArray()), datagramPacket.sender());
            ctx.writeAndFlush(datagramPacketResponse);
            log.info(datagramPacketResponse.toString());
        }else{
            ctx.fireChannelRead(map);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("握手时产生异常");
        super.exceptionCaught(ctx, cause);
    }
}
