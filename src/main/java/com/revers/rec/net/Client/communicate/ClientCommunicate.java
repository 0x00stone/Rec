package com.revers.rec.net.Client.communicate;

import com.alibaba.fastjson.JSON;
import com.revers.rec.Kademlia.Bucket.RoutingTable;
import com.revers.rec.Kademlia.Node.KademliaId;
import com.revers.rec.Kademlia.Node.Node;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.config.optionConfig;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.net.PortUnreachableException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

@Slf4j
public class ClientCommunicate implements Callable<Data> {
    @Autowired
    private RoutingTable routingTable;
    private Object[] objects = null;
    boolean routingTableHasValue = true;


    public ClientCommunicate(Data data) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        this.routingTable = BeanContextUtil.getBean(RoutingTable.class);

        String dataString = JSON.toJSONString(data);

        List<Node> closest = routingTable.findClosest(new KademliaId(DigestUtil.Sha1AndSha256(data.getDestPublicKey())));
        if(closest.size() == 0 || closest == null) {
            log.info("路由表中没有找到节点");
            routingTableHasValue = false;
            return;
        }
        Node toNode = closest.get(0);
        String aes = toNode.getAesKey();


        Long order = new Random().nextLong();
        MsgProtobuf.Connection connection = MsgProtobuf.Connection.newBuilder()
                .setSrcPublicKey(RsaUtil.publicEncrypt(AccountConfig.getPublicKey(), toNode.getPublicKey()))
                .setDestPublicKey(toNode.getPublicKey())
                .setOrder(order)
                .setMsgType(ConstantUtil.MSGTYPE_COMMUNICATE)
                .setSignature(RsaUtil.privateEncrypt(Sha256Util.getSHA256(dataString), AccountConfig.getPrivateKey()))
                .setTimestamp(System.currentTimeMillis())
                .setData(AesUtil.encrypt(aes, dataString))
                .build();

        objects = new Object[5];
        objects[0] = connection;
        objects[1] = toNode.getInetAddress();
        objects[2] = toNode.getPort();
        objects[3] = order;
        objects[4] = aes;
    }
    @Override
    public Data call() throws Exception {
        if(!routingTableHasValue){
            return null;
        }
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
                            pipeline.addFirst(new ProtobufVarint32FrameDecoder());
                            pipeline.addFirst(new ProtobufDecoder(MsgProtobuf.Connection.getDefaultInstance()));
                            pipeline.addFirst(new ProtobufVarint32LengthFieldPrepender());
                            pipeline.addFirst(new ProtobufEncoder());
                            pipeline.addLast(new ClientCommunicateHandler());
                        }
                    });
            Channel ch= b.connect((String)objects[1], (Integer) objects[2]).sync().channel();

            ch.attr(AttributeKey.valueOf("orderOrigin")).set(objects[3]);
            ch.attr(AttributeKey.valueOf("aes")).set(objects[4]);

            DatagramPacket datagramPacket = new DatagramPacket(
                    Unpooled.copiedBuffer(((MsgProtobuf.Connection) objects[0]).toByteArray()),
                    SocketUtils.socketAddress((String)objects[1], (Integer) objects[2]));
            ch.writeAndFlush(datagramPacket).sync();
            log.info(datagramPacket.toString());


            if(!ch.closeFuture().await(optionConfig.getClientCommunicateRunTimeOut())){
                System.out.println("Time Out");
                return null;
            }
            while (ch.attr(AttributeKey.valueOf("data")).get() == null){}

            if(ch.attr(AttributeKey.valueOf("data")).get() != null){
                Data data = new Data();
                data.setData((String) ch.attr(AttributeKey.valueOf("data")).get());
                return data;
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
        return null;
    }
}
