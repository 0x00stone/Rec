package com.revers.rec.net.Client.communicate;

import com.alibaba.fastjson.JSON;
import com.revers.rec.Kademlia.Bucket.RoutingTable;
import com.revers.rec.Kademlia.Bucket.RoutingTableImpl;
import com.revers.rec.Kademlia.Node.KademliaId;
import com.revers.rec.Kademlia.Node.Node;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.Data;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.net.Client.ping.ClientPingHandler;
import com.revers.rec.util.BeanContextUtil;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.ResultUtil;
import com.revers.rec.util.cypher.AesUtil;
import com.revers.rec.util.cypher.DigestUtil;
import com.revers.rec.util.cypher.RsaUtil;
import com.revers.rec.util.cypher.Sha256Util;
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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * @author Revers.
 * @date 2022/04/20 22:22
 **/
public class ClientCommunicate implements Callable<ResultUtil> {
    @Autowired
    private RoutingTable routingTable;

    private ArrayList<Object[]> toNodeList = null; //[MsgProtobuf.Connection[] connection,String HOST,int PORT]

    public ClientCommunicate(String destPublicKey, String content) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        toNodeList = new ArrayList<>();
        this.routingTable = BeanContextUtil.getBean(RoutingTable.class);

        Data data = new Data();
        data.setSrcPublicKey(AccountConfig.getPublicKey());
        data.setDestPublicKey(destPublicKey);
        data.setData(RsaUtil.publicEncrypt(destPublicKey,content));
        data.setSignature(Sha256Util.getSHA256(data.getData()));
        data.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        String dataString = JSON.toJSONString(data);

        for(Node toNode : routingTable.findClosest(new  KademliaId(DigestUtil.Sha1AndSha256(destPublicKey)))){
            Object[] objects = new Object[3];
            String aes = toNode.getAesKey();



            MsgProtobuf.Connection connection = MsgProtobuf.Connection.newBuilder()
                    .setSrcPublicKey(AccountConfig.getPublicKey())
                    .setDestPublicKey(toNode.getPublicKey())
                    .setOrder(new Random().nextLong())
                    .setMsgType(ConstantUtil.MSGTYPE_COMMUNICATE)
                    .setSignature(RsaUtil.privateEncrypt(Sha256Util.getSHA256(dataString),AccountConfig.getPrivateKey()))
                    .setTimestamp(System.currentTimeMillis())
                    .setData(AesUtil.encrypt(aes,dataString))
                    .build();

            objects[0] = connection;
            objects[1] = toNode.getInetAddress();
            objects[2] = toNode.getPort();

            toNodeList.add(objects);
        }
    }
//TODO 数据通信的client 和 server
    @Override
    public ResultUtil call() {
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

            if(!ch.closeFuture().await(150000)){
                System.out.println("Time Out");
                return new ResultUtil(false,"Time Out");
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
                return new ResultUtil(true, "Ping成功");
            }else {
                return new ResultUtil(false, "Ping失败");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
        return new ResultUtil(false, "发送失败");
    }
}
