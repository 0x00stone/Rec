package com.revers.rec.controller.Server;

import com.revers.rec.domain.Data;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.util.ConstantUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
@Slf4j
public class ServerDataHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HashMap<String,Object> map = (HashMap<String, Object>) msg;
        MsgProtobuf.Connection connection = (MsgProtobuf.Connection)map.get("connection");
        DatagramPacket datagramPacket = (DatagramPacket) map.get("datagramPacket");

        if(connection != null){
            if(connection.getMsgType() == ConstantUtil.MSGTYPE_TYPE2){
                log.info("ServerHandShakeType2Handler收到非type2消息");
                //TODO 处理type2消息
                ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                        ConstantUtil.CONNECTION_GETTING_PING, CharsetUtil.UTF_8),datagramPacket.sender()));
            }else {
                log.info("ServerHandShakeType2Handler收到非type2消息");
                ctx.fireChannelRead(map);
            }
        }else {
            log.info("ServerHandShakeType2Handler收到空消息");
            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    ConstantUtil.CONNECTION_GETTING_NONE, CharsetUtil.UTF_8),datagramPacket.sender()));
        }

        /**log.info("ServerHandler收到消息");
        MsgProtobuf.connection connection = MsgProtobuf.connection.parseFrom(datagramPacket.content().nioBuffer());

        System.out.println("监听到srcId:" + connection.getSrcId() + "的消息" + connection.getData());
        if (connection.getDestId() == AccountConfig.getId() && connection.getDestPublicKey() == AccountConfig.getPublicKey()) {
            String s = Rsa.privateDecrypt(connection.getData(), AccountConfig.getPrivateKey());
            Data data = JSON.parseObject(s, Data.class);
            HashMap<String,Object> map = new HashMap();
            map.put("data",data);
            map.put("datagramPacket",datagramPacket);
            channelHandlerContext.fireChannelRead(map);
        }else{
            //TODO 转发消息
            //判断该节点到目标节点的距离
            KademliaId kademliaId = new KademliaId(connection.getDestId());
            KademliaId localKademliaId = new KademliaId(AccountConfig.getId());
            int distance = kademliaId.getDistance(localKademliaId);
            if(distance < 5){
                //判断是否在线,在线转发,不在线存储
                //if(RoutingTableImpl)
            }else{
                //转发
            }
        }**/
    }

}
