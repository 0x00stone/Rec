package com.revers.rec.net.Server.handShake;

import com.revers.rec.Kademlia.Bucket.RoutingTable;
import com.revers.rec.Kademlia.Node.KademliaId;
import com.revers.rec.Kademlia.Node.Node;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.ConnectKey;
import com.revers.rec.domain.Connect;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.service.connectKey.ConnectKeyService;
import com.revers.rec.util.BeanContextUtil;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.cypher.AesUtil;
import com.revers.rec.util.cypher.DigestUtil;
import com.revers.rec.util.cypher.RsaUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * @author Revers.
 * @date 2022/04/19 23:49
 **/
@Slf4j
public class HandShakeServerHandler3 extends ChannelInboundHandlerAdapter {
    @Autowired
    private RoutingTable routingTable;
    @Autowired
    private ConnectKeyService connectKeyService;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HashMap<String, Object> map = (HashMap<String, Object>) msg;
        MsgProtobuf.Connection connection = (MsgProtobuf.Connection) map.get("connection");
        DatagramPacket datagramPacket = (DatagramPacket) map.get("datagramPacket");
        if(connection.getMsgType() == ConstantUtil.MSGTYPE_HANDSHAKE_3){
            String publicKey = (String)ctx.attr(AttributeKey.valueOf("publicKey")).get();
            String id = DigestUtil.Sha1AndSha256(publicKey);
            String AES = RsaUtil.privateDecrypt(connection.getData(),AccountConfig.getPrivateKey());
            ctx.attr(AttributeKey.valueOf("aes")).set(AES);

            this.routingTable = BeanContextUtil.getBean(RoutingTable.class);
            this.connectKeyService = BeanContextUtil.getBean(ConnectKeyService.class);
            routingTable.insert(new Node(new KademliaId(DigestUtil.Sha1AndSha256(publicKey)),connection.getIpv6(),
                    Integer.valueOf(connection.getPort()),publicKey,AES  ));

            //TODO 判断是否存在连接密钥 ,存在更新,不存在创建
            ConnectKey connectKey = connectKeyService.getConnectKey(id);
            if(connectKey == null){
                //创建连接密钥
                connectKey = new ConnectKey();
                connectKey.setId(DigestUtil.Sha1AndSha256(publicKey));
                connectKey.setAesKey(AES);
                connectKey.setPublicKey(publicKey);
                connectKey.setTimeStamp(System.currentTimeMillis());
                connectKeyService.saveConnectKey(connectKey);
            }else {
                //更新连接密钥
                connectKey.setAesKey(AES);
                connectKey.setTimeStamp(System.currentTimeMillis());
                connectKeyService.updateConnectKey(connectKey);
            }

            Connect connectResponse = new Connect();
            connectResponse.setConnectOrder(connection.getOrder()+1);
            connectResponse.setConnectMsgType(ConstantUtil.MSGTYPE_HANDSHAKE_4);
            connectResponse.setConnectData(AesUtil.encrypt(AES,"success"));
            connectResponse.setConnectIpv6Ip(AccountConfig.getIpv6());
            connectResponse.setConnectIpv6Port(String.valueOf(AccountConfig.getIpv6Port()));
            connectResponse.setConnectTimestamp(System.currentTimeMillis());

            MsgProtobuf.Connection connectionResponse = MsgProtobuf.Connection.newBuilder()
                    .setOrder(connectResponse.getConnectOrder())
                    .setMsgType(connectResponse.getConnectMsgType())
                    .setData(connectResponse.getConnectData())
                    .setIpv6(connectResponse.getConnectIpv6Ip())
                    .setPort(connectResponse.getConnectIpv6Port())
                    .setTimestamp(connectResponse.getConnectTimestamp())
                    .setSignature(connectResponse.getSignature())
                    .build();

            DatagramPacket datagramPacketResponse = new DatagramPacket(Unpooled.copiedBuffer(
                    connectionResponse.toByteArray()), datagramPacket.sender());
            ctx.writeAndFlush(datagramPacketResponse);
            log.info(datagramPacketResponse.toString());
        }else {
            super.channelRead(ctx, msg);
        }
    }
}
