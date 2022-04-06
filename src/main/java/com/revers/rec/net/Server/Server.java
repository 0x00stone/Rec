package com.revers.rec.net.Server;

import com.revers.rec.domain.protobuf.MsgProtobuf;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Server implements Runnable{
    private static final int PORT = 7686;

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
                            pipeline.addLast(new ServerHandler());
                        }
                    });

            System.out.println("服务端已启动...");
            b.bind(PORT).sync().channel().closeFuture().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
            System.out.println("服务端已关闭...");
        }
    }
}
