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
public class ClientCommunicate implements Callable<Data> {
    @Autowired
    private RoutingTable routingTable;

    private ArrayList<Object[]> toNodeList = null; //[MsgProtobuf.Connection[] connection,String HOST,int PORT,Integer order,String aes]

    public ClientCommunicate(Data data) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        toNodeList = new ArrayList<>();
        this.routingTable = BeanContextUtil.getBean(RoutingTable.class);

        String dataString = JSON.toJSONString(data);

        for(Node toNode : routingTable.findClosest(new  KademliaId(DigestUtil.Sha1AndSha256(data.getDestPublicKey())))){
            Object[] objects = new Object[5];
            String aes = toNode.getAesKey();


            Long order = new Random().nextLong();
            MsgProtobuf.Connection connection = MsgProtobuf.Connection.newBuilder()
                    .setSrcPublicKey(RsaUtil.publicEncrypt(AccountConfig.getPublicKey(),toNode.getPublicKey()))
                    .setDestPublicKey(toNode.getPublicKey())
                    .setOrder(order)
                    .setMsgType(ConstantUtil.MSGTYPE_COMMUNICATE)
                    .setSignature(RsaUtil.privateEncrypt(Sha256Util.getSHA256(dataString),AccountConfig.getPrivateKey()))
                    .setTimestamp(System.currentTimeMillis())
                    .setData(AesUtil.encrypt(aes,dataString))
                    .build();

            objects[0] = connection;
            objects[1] = toNode.getInetAddress();
            objects[2] = toNode.getPort();
            objects[3] = order;
            objects[4] = aes;

            toNodeList.add(objects);
        }
    }

    @Override
    public Data call() {
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
                            pipeline.addLast(new ClientCommunicateHandler());
                        }
                    });

            Channel[] channels = new Channel[toNodeList.size()];
            int i = 0;
            for(Object[] objects : toNodeList) {
                channels[i] = b.connect((String) objects[1], (Integer) objects[2]).sync().channel();

                channels[i].writeAndFlush(new DatagramPacket(
                        Unpooled.copiedBuffer(((MsgProtobuf.Connection)objects[0]).toByteArray()),
                        SocketUtils.socketAddress((String) objects[1], (Integer) objects[2]))).sync();
                System.out.println("已向 " + i +" 通道发送数据包");

                channels[i].attr(AttributeKey.valueOf("orderOrigin")).set(objects[3]);
                channels[i].attr(AttributeKey.valueOf("aes")).set(objects[4]);
                ++i;
            }

            for(int j = 0 ; j < i ; j++){
                if(!channels[j].closeFuture().await(150000)){
                    System.out.println(j + " Channel Time Out");
                    channels[j].close();
                    continue;
                }

                if(channels[j].attr(AttributeKey.valueOf("data")).get() != null){
                    return (Data) channels[j].attr(AttributeKey.valueOf("data")).get();
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
            return null;
        }

    }
}
