package com.revers.rec.net.Client;

import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.util.Result;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.util.AttributeKey;
import io.netty.util.internal.SocketUtils;

import java.util.concurrent.Callable;

public final class Client implements Callable<Result> {
    private static String Host;
    private static int PORT;
    private static MsgProtobuf.Connection connection;

    public Client(String host, int port, MsgProtobuf.Connection connection){
        this.Host = host;
        this.PORT = port;
        this.connection = connection;
    }

    @Override
    public Result call() {
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
                            pipeline.addFirst(new ProtobufVarint32FrameDecoder());
                            pipeline.addFirst(new ProtobufDecoder(MsgProtobuf.Connection.getDefaultInstance()));
                            pipeline.addFirst(new ProtobufVarint32LengthFieldPrepender());
                            pipeline.addFirst(new ProtobufEncoder());
                            pipeline.addLast(new ClientHandler());
                        }
                    });
            Channel ch= b.connect(Host, PORT).sync().channel();

            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer(connection.toByteArray()),
                    SocketUtils.socketAddress(Host, PORT))).sync();
            System.out.println("已发送");;
            if(!ch.closeFuture().await(15000)){
                System.out.println("发送超时");
                return new Result(false,"发送超时");
            }
            while (ch.attr(AttributeKey.valueOf("result")).get() == null){}
            if("201".equals(ch.attr(AttributeKey.valueOf("result")).get())){
                return new Result(true, "发送失败");
            }
            if ("401".equals(ch.attr(AttributeKey.valueOf("result")).get())){
                return new Result(false, "发送失败");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
            return new Result(false, "发送失败");
        }
    }

}