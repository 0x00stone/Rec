package com.revers.rec.net.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.SocketUtils;

public final class Client implements Runnable{

    static final int PORT = 7686;

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new ClientHandler());

            Channel ch = b.connect("127.0.0.1",PORT).sync().channel();

            // Broadcast the QOTM request to port 8080.
            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("QOTM?", CharsetUtil.UTF_8),
                    SocketUtils.socketAddress("127.0.0.1", PORT))).sync();

            // TheMomentClientHandler will close the DatagramChannel when a
            // response is received.  If the channel is not closed within 5 seconds,
            // print an error message and quit.
            if (!ch.closeFuture().await(5000)) {
                System.err.println("QOTM request timed out.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}