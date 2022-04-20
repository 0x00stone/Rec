package com.revers.rec.net.Client.ping;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.net.Client.ClientHandler;
import com.revers.rec.util.ConstantUtil;
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

import java.util.Random;
import java.util.concurrent.Callable;

public final class ClientPing implements Callable<Result> {
    private static MsgProtobuf.Connection connection;
    private String HOST;
    private int PORT;

    public ClientPing(String host, int port){
        this.HOST = host;
        this.PORT = port;

        this.connection = MsgProtobuf.Connection.newBuilder()
                .setOrder(new Random().nextLong())
                .setMsgType(0)
                .setData("ping")
                .build();
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
                            pipeline.addLast(new ClientPingHandler());
                        }
                    });
            Channel ch= b.connect(HOST,PORT).sync().channel();

            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer(connection.toByteArray()),
                    SocketUtils.socketAddress(HOST,PORT))).sync();
            System.out.println("已发送Ping消息");

            if(!ch.closeFuture().await(15000)){
                System.out.println("Time Out");
                return new Result(false,"Time Out");
            }
            while (ch.attr(AttributeKey.valueOf("data")).get() == null){}
            boolean isPingSuccess = false;
            if(ch.attr(AttributeKey.valueOf("msgType")).get() == ConstantUtil.MSGTYPE_PING_RESPONSE){
                if((Long)ch.attr(AttributeKey.valueOf("order")).get() == connection.getOrder()+1){
                    if(ConstantUtil.PING_SUCCESS.equals(ch.attr(AttributeKey.valueOf("data")).get()) ){
                        isPingSuccess = true;
                    }
                }
            }
            if(isPingSuccess){
                return new Result(true, "Ping成功");
            }else {
                return new Result(false, "Ping失败");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
        return new Result(false, "发送失败");
    }

}
