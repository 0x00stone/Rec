package com.revers.rec.net.Server;

import com.revers.rec.config.optionConfig;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.net.Server.communicate.ServerCommunicateHandler;
import com.revers.rec.net.Server.handShake.HandShakeServerHandler1;
import com.revers.rec.net.Server.handShake.HandShakeServerHandler3;
import com.revers.rec.net.Server.ping.ServerPingHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.util.concurrent.Callable;

public class Server implements Callable<Boolean> {

    @Override
    public Boolean call() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap()
                    .group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        public void initChannel(Channel ch){
                            ChannelPipeline pipeline=ch.pipeline();
                            pipeline.addLast(new ProtobufVarint32FrameDecoder());
                            pipeline.addLast(new ProtobufDecoder(MsgProtobuf.Connection.getDefaultInstance()));
                            pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                            pipeline.addLast(new ProtobufEncoder());
                            pipeline.addLast(new ServerPingHandler());
                            pipeline.addLast(new HandShakeServerHandler1());
                            pipeline.addLast(new HandShakeServerHandler3());
                            pipeline.addLast(new ServerCommunicateHandler());
                        }
                    });

            System.out.println("==========================服务端已启动,监听"+optionConfig.getServerListenPort()+"端口中===========================");

            boolean isListening = false;
            while (!isListening) {
                try {
                    b.bind(optionConfig.getServerListenPort()).sync().channel().closeFuture().await();
                    isListening = true;
                } catch (Exception e) {
                    System.console().flush();
                    System.out.println("==========================端口" + optionConfig.getServerListenPort() + "已被占用===========================");
                    optionConfig.setServerListenPort(optionConfig.getServerListenPort() + 1);
                    System.out.println("==========================开启监听端口" + optionConfig.getServerListenPort() + "===========================");
                }
            }


        } catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
            System.out.println("=============================服务端已关闭==============================");
            return false;
        }
    }
}
