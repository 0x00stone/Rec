package com.revers.rec.Client.Handler;/*
package com.revers.rec.Client.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class ClientMsgHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ClientMsgHandler.class);

    @Autowired
    LocalService service;

    @Autowired
    AccountService accountService;

    @Autowired
    MimClientConfig appConfig;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        accountService.logout(appConfig.getUserId(), appConfig.getUsername());
        System.out.println("已经断开连接... remote:" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) {
        MimProtocol protocol = (MimProtocol) object;

        // 消息回应
        if (protocol.getType() != null && protocol.getType().equals(MsgType.MSG_RESP)) {
            service.printNormal(protocol);
//            System.out.println(protocol);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}*/
