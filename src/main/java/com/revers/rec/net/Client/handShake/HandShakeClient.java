package com.revers.rec.net.Client.handShake;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.config.optionConfig;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.ResultUtil;
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

import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * @author Revers.
 * @date 2022/04/19 22:59
 **/

public class HandShakeClient implements Callable<ResultUtil> {
    private MsgProtobuf.Connection connection;
    private String HOST;
    private Integer PORT;

    public HandShakeClient(String host, int port) throws NoSuchAlgorithmException {
        this.HOST = host;
        this.PORT = port;
        this.connection = MsgProtobuf.Connection.newBuilder()
                .setMsgType(ConstantUtil.MSGTYPE_HANDSHAKE_1)
                .setOrder(new Random().nextLong())
                .setData(AccountConfig.getPublicKey())
                .build();
    }

    @Override
    public ResultUtil call() throws Exception {
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
                            pipeline.addLast(new HandShakeClientHandler2());
                            pipeline.addLast(new HandShakeClientHandler4());
                        }
                    });
            Channel ch= b.connect(HOST,PORT).sync().channel();

            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer(connection.toByteArray()),
                    SocketUtils.socketAddress(HOST,PORT))).sync();
            System.out.println("已发送");

            if(!ch.closeFuture().await(optionConfig.getClientHandShakeClientRunTimeOut())){
                return new ResultUtil(false,"Time Out");
            }
            while (ch.attr(AttributeKey.valueOf("isSuccess")).get() == null){}

            if((boolean)ch.attr(AttributeKey.valueOf("isSuccess")).get() == true){
                return new ResultUtil(true,"连接成功");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
        return new ResultUtil(false, "连接失败");
    }
}
