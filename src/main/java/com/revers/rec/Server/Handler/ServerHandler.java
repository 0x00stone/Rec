package com.revers.rec.Server.Handler;/*
package com.revers.rec.Server.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import cn.sawyer.mim.tool.enums.MsgType;
import cn.sawyer.mim.tool.protocol.MimProtocol;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接断开：" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) {

        MimProtocol protocol = (MimProtocol) obj;
        if (protocol.getType().equals(MsgType.MSG_REQ)) {
            System.out.println("[+] 服务器收到：" + protocol);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("[!] ServerHandler channelRead错误");
        cause.printStackTrace();
    }
}

*/
