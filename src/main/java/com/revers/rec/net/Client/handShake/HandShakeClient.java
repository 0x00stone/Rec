package com.revers.rec.net.Client.handShake;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.config.optionConfig;
import com.revers.rec.domain.Connect;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.net.Client.SignatureMatchHandler;
import com.revers.rec.net.TimeStampHandler;
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
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * @author Revers.
 * @date 2022/04/19 22:59
 **/
@Slf4j
public class HandShakeClient implements Callable<Result> {
    private MsgProtobuf.Connection connection;
    private String HOST;
    private Integer PORT;

    public HandShakeClient(String host, int port) throws NoSuchAlgorithmException {
        this.HOST = host;
        this.PORT = port;

        Connect connect = new Connect();
        connect.setConnectOrder(new Random().nextLong());
        connect.setConnectMsgType(ConstantUtil.MSGTYPE_HANDSHAKE_1);
        connect.setConnectTimestamp(System.currentTimeMillis());
        connect.setConnectData(AccountConfig.getPublicKey());

        this.connection = MsgProtobuf.Connection.newBuilder()
                .setMsgType(connect.getConnectMsgType())
                .setOrder(connect.getConnectOrder())
                .setData(connect.getConnectData())
                .setTimestamp(connect.getConnectTimestamp())
                .setSignature(connect.getSignature())
                .build();
    }

    @Override
    public Result call(){
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
                            pipeline.addLast(new SignatureMatchHandler());
                            pipeline.addLast(new TimeStampHandler());
                            pipeline.addLast(new HandShakeClientHandler2());
                            pipeline.addLast(new HandShakeClientHandler4());
                        }
                    });
            Channel ch= b.connect(HOST,PORT).sync().channel();

            DatagramPacket datagramPacket = new DatagramPacket(
                    Unpooled.copiedBuffer(connection.toByteArray()),
                    SocketUtils.socketAddress(HOST, PORT));
            ch.writeAndFlush(datagramPacket).sync();
            log.info(datagramPacket.toString());

            if(!ch.closeFuture().await(optionConfig.getClientHandShakeClientRunTimeOut())){
                return new Result(ConstantUtil.ERROR,"Time Out");
            }
            while (ch.attr(AttributeKey.valueOf("isSuccess")).get() == null){}

            if((boolean)ch.attr(AttributeKey.valueOf("isSuccess")).get() == true){
                return new Result(ConstantUtil.SUCCESS,"连接成功");
            }
        } catch (InterruptedException e) {
            System.out.println("对方拒绝连接");
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
        return new Result(ConstantUtil.ERROR, "连接失败");
    }
}
