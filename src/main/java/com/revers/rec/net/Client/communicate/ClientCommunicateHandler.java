package com.revers.rec.net.Client.communicate;

import com.alibaba.fastjson.JSON;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.Data;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.cypher.AesUtil;
import com.revers.rec.util.cypher.RsaUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientCommunicateHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object datagramPacket) throws Exception {
        log.info(datagramPacket.toString());
        MsgProtobuf.Connection connection = MsgProtobuf.Connection.parseFrom(((DatagramPacket)datagramPacket).content().nioBuffer());

        if(connection.getMsgType() == ConstantUtil.MSGTYPE_COMMUNICATE){
            String aes = (String)channelHandlerContext.attr(AttributeKey.valueOf("aes")).get();
            String responseDataString = AesUtil.decrypt(aes,connection.getData());
            Data responseData = JSON.parseObject(responseDataString,Data.class);
            String data = RsaUtil.privateDecrypt(responseData.getData(), AccountConfig.getPrivateKey());
            System.out.println("收到服务器消息："+data);
            channelHandlerContext.attr(AttributeKey.valueOf("data")).set(data);
        }

        channelHandlerContext.close();
    }

}
