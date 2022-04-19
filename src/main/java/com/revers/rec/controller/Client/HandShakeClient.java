package com.revers.rec.controller.Client;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.service.net.Session;
import com.revers.rec.util.Result;
import com.revers.rec.util.cypher.Aes;
import com.revers.rec.util.cypher.DigestUtil;
import com.revers.rec.util.cypher.Rsa;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * @author Revers.
 * @date 2022/04/19 22:59
 **/


public class HandShakeClient implements Callable<Result> {
    private MsgProtobuf.Connection connection;
    private String AES;


    @Autowired
    Session session;

    public HandShakeClient(String host, int port,String destPublicKey) throws NoSuchAlgorithmException {
        AES = Aes.getAseKey(256);
        this.connection = MsgProtobuf.Connection.newBuilder()
                .setSrcPublicKey(AccountConfig.getPublicKey())
                .setDestPublicKey(destPublicKey)
                .setPort(String.valueOf(port))
                .setIpv6(host)
                .setOrder(new Random().nextLong())
                .setMsgType(1)
                .setSignature("")
                .setTimestamp(0)
                .setData(AES)
                .build();
    }

    @Override
    public Result call() throws Exception {
        int flag = -1; //标志是否握手成功 // -1 初始值 // 0 失败 // 1 成功
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
                            pipeline.addLast(new HandShakeClientHandler2());
                            pipeline.addLast(new HandShakeClientHandler4());
                        }
                    });
            Channel ch= b.connect(connection.getIpv6(),Integer.valueOf(connection.getPort())).sync().channel();

            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer(connection.toByteArray()),
                    SocketUtils.socketAddress(connection.getIpv6(),Integer.valueOf(connection.getPort())))).sync();
            System.out.println("已发送");
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
            if(flag == 1){
                System.out.println("握手成功");
                String Aes = Rsa.privateDecrypt(connection.getData(), AccountConfig.getPrivateKey());
                Object[] objects = new Object[3];
                objects[0] = connection.getMsgType();
                objects[1] = connection.getDestPublicKey();
                objects[2] = AES;
                session.handShakeSession.put(DigestUtil.Sha1AndSha256(connection.getDestPublicKey()),objects);
            }
            return new Result(false, "发送失败");
        }
    }
}
