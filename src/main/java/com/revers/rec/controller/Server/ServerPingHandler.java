package com.revers.rec.controller.Server;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.protobuf.MsgProtobuf.Connection;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.cypher.Md5;
import com.revers.rec.util.cypher.Rsa;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**  处理ping消息 **/
@Slf4j
public class ServerPingHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        Connection connection = Connection.parseFrom(datagramPacket.content().nioBuffer());
        HashMap<String,Object> map = new HashMap<>();
        map.put("connection",connection);
        map.put("datagramPacket",datagramPacket);

        if(connection != null){
            if(AccountConfig.getPublicKey().equals(connection.getDestPublicKey())){//这个包是不是发给自己
                if(connection.getMsgType() == ConstantUtil.MSGTYPE_PING){
                    log.info("ServerPingHandler收到ping消息");
                    channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                            ConstantUtil.CONNECTION_GETTING_PING, CharsetUtil.UTF_8),datagramPacket.sender()));
                }else {
                    if(Md5.md5(connection.getData()).equals(Rsa.publicDecrypt(connection.getSignature(),connection.getSrcPublicKey()))){
                        //签名是否正确
                        channelHandlerContext.fireChannelRead(map);
                    }else{
                        log.info("ServerPingHandler收到签名错误消息");
                        channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                                ConstantUtil.CONNECTION_SIGNATURE_ERROR, CharsetUtil.UTF_8),datagramPacket.sender()));
                    }
                }
            }else{
                log.info("ServerPingHandler不为包接收者");
                channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                        ConstantUtil.CONNECTION_CANT_RECEIVE, CharsetUtil.UTF_8),datagramPacket.sender()));
            }
        }else {
            log.info("ServerPingHandler收到空消息");
            channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    ConstantUtil.CONNECTION_GETTING_NONE, CharsetUtil.UTF_8),datagramPacket.sender()));
        }

    }
}
