package com.revers.rec.net.Client;

import com.revers.rec.domain.Connection;
import com.revers.rec.domain.Data;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.util.Byte;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.SocketUtils;

import java.nio.charset.StandardCharsets;

public final class Client implements Runnable{
    private static String Host;
    private static int PORT;
    private static Connection connection;

    public Client(String host, int port, Connection connection){
        this.Host = host;
        this.PORT = port;
        this.connection = connection;
    }

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap()
                    .group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, false)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        public void initChannel(Channel ch){
                            ChannelPipeline pipeline=ch.pipeline();
                            //往pipeline链中添加一个解码器
                            pipeline.addLast("decoder",new StringDecoder());
                            //往pipeline链中添加一个编码器
                            pipeline.addLast("encoder",new StringEncoder());
                            pipeline.addLast(new ClientHandler());
                        }
                    });
            Channel ch= b.connect(Host, PORT).sync().channel();

            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer(Byte.objectToBytes(connection)),
                    SocketUtils.socketAddress(Host, PORT))).sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
}