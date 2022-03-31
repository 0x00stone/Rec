/*
package com.revers.rec.Server.Handler;

import cn.sawyer.mim.tool.enums.MsgType;
import cn.sawyer.mim.tool.protocol.MimProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Date;

public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        MimProtocol protocol = (MimProtocol) msg;
        // 返回心跳应答消息
        if (protocol.getType() == MsgType.HEARTBEAT_REQ) {
            MimProtocol heartBeat = buildHeatBeat();
            System.out.println("响应心跳： " + ctx.channel().remoteAddress() +  ": ---> " + new Date().toString());
            ctx.writeAndFlush(heartBeat);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

    // 构建心跳响应
    private MimProtocol buildHeatBeat() {
        MimProtocol protocol = new MimProtocol();
        protocol.setType(MsgType.HEARTBEAT_RESP);

        return protocol;
    }
}
*/
