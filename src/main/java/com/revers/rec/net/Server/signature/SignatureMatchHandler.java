package com.revers.rec.net.Server.signature;

import com.revers.rec.domain.Connect;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.util.ConstantUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class SignatureMatchHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HashMap<String, Object> map = (HashMap<String, Object>) msg;
        MsgProtobuf.Connection connection = (MsgProtobuf.Connection) map.get("connection");
        DatagramPacket datagramPacket = (DatagramPacket) map.get("datagramPacket");

        Connect connectRequest = new Connect();
        connectRequest.setConnectOrder(connection.getOrder());
        connectRequest.setConnectMsgType(connection.getMsgType());
        connectRequest.setConnectTimestamp(connection.getTimestamp());
        if(connection.getSrcPublicKey() != null && !"".equals(connection.getSrcPublicKey())){
            connectRequest.setConnectSrcPublicKey(connection.getSrcPublicKey());
        }
        if(connection.getDestPublicKey() != null && !"".equals(connection.getDestPublicKey())){
            connectRequest.setConnectDestPublicKey(connection.getDestPublicKey());
        }
        if(connection.getPort() != null && !"".equals(connection.getPort())){
            connectRequest.setConnectIpv6Port(connection.getPort());
        }
        if(connection.getIpv6() != null && !"".equals(connection.getIpv6())){
            connectRequest.setConnectIpv6Ip(connection.getIpv6());
        }
        if(connection.getData() != null && !"".equals(connection.getData())){
            connectRequest.setConnectData(connection.getData());
        }


        if(connectRequest.getSignature().equals(connection.getSignature())){
            //验证通过
            if(ConstantUtil.FAILURE_SIGNATURE_MISMATCH.equals(connection.getMsgType())){
                //验证失败
                log.info("对方签名校验失败");
            }else {
                ctx.fireChannelRead(map);
            }
        }else{
            //验证失败
            log.info("签名校验失败");
            Connect connectResponse = new Connect();
            connectResponse.setConnectOrder(connection.getOrder()+1);
            connectResponse.setConnectMsgType(ConstantUtil.FAILURE_SIGNATURE_MISMATCH);
            connectResponse.setConnectTimestamp(System.currentTimeMillis());

            MsgProtobuf.Connection connectionResponse = MsgProtobuf.Connection.newBuilder()
                    .setOrder(connectResponse.getConnectOrder())
                    .setMsgType(connectResponse.getConnectMsgType())
                    .setTimestamp(connectResponse.getConnectTimestamp())
                    .setSignature(connectResponse.getSignature())
                    .build();

            DatagramPacket datagramPacketResponse = new DatagramPacket(Unpooled.copiedBuffer(
                    connectionResponse.toByteArray()), datagramPacket.sender());
            ctx.writeAndFlush(datagramPacketResponse);
            log.info(datagramPacketResponse.toString());
        }
    }
}
