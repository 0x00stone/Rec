package com.revers.rec.net.Server;

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
    public static final int PORT = 30000;



    @Override
    public Boolean call() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap()
                    .group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        public void initChannel(Channel ch){
                            ChannelPipeline pipeline=ch.pipeline();
                            pipeline.addFirst(new ProtobufVarint32FrameDecoder());
                            pipeline.addFirst(new ProtobufDecoder(MsgProtobuf.Connection.getDefaultInstance()));
                            pipeline.addFirst(new ProtobufVarint32LengthFieldPrepender());
                            pipeline.addFirst(new ProtobufEncoder());
                            pipeline.addLast(new ServerPingHandler());
                            pipeline.addLast(new HandShakeServerHandler1());
                            pipeline.addLast(new HandShakeServerHandler3());
                            pipeline.addLast(new ServerCommunicateHandler());
                        }
                    });

            System.out.println("==========================服务端已启动,监听"+PORT+"端口中===========================");
            b.bind(PORT).sync().channel().closeFuture().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
            System.out.println("=============================服务端已关闭==============================");
            return false;
        }
    }
}
