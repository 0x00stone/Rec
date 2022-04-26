package com.revers.rec.net;

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
public class TimeStampHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HashMap<String, Object> map = (HashMap<String, Object>) msg;
        MsgProtobuf.Connection connection = (MsgProtobuf.Connection) map.get("connection");
        DatagramPacket datagramPacket = (DatagramPacket) map.get("datagramPacket");

        if(connection.getTimestamp() > System.currentTimeMillis()){
            //时间戳有误
            Connect connectResponse = new Connect();
            connectResponse.setConnectOrder(connection.getOrder()+1);
            connectResponse.setConnectMsgType(ConstantUtil.FAILURE_TIMESTAMP);
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
            log.info("时间戳有误 : 报文时间戳" + connection.getTimestamp()  + " 当前时间戳" + System.currentTimeMillis());
            log.info(datagramPacketResponse.toString());
        }else{
            //时间戳正确
            if(ConstantUtil.FAILURE_TIMESTAMP == connection.getMsgType()){
                log.info("对方接收时间戳有误");
            }else{
                ctx.fireChannelRead(map);
            }
        }
    }
}
