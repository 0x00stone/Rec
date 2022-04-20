package com.revers.rec.net.Client.handShake;

import com.revers.rec.Kademlia.Bucket.RoutingTable;
import com.revers.rec.Kademlia.Node.KademliaId;
import com.revers.rec.Kademlia.Node.Node;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.util.BeanContextUtil;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.cypher.AesUtil;
import com.revers.rec.util.cypher.DigestUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * @author Revers.
 * @date 2022/04/19 23:11
 **/
public class HandShakeClientHandler4 extends ChannelInboundHandlerAdapter {
    @Autowired
    private RoutingTable routingTable;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HashMap<String, Object> map = (HashMap<String, Object>) msg;
        MsgProtobuf.Connection connection = (MsgProtobuf.Connection) map.get("connection");


        if(connection.getMsgType() == ConstantUtil.MSGTYPE_HANDSHAKE_4){
            String publicKey = (String)ctx.attr(AttributeKey.valueOf("publicKey")).get();
            String AES = (String)ctx.attr(AttributeKey.valueOf("aes")).get();

            if("success".equals(AesUtil.decrypt(AES,connection.getData()))) {
                this.routingTable = BeanContextUtil.getBean(RoutingTable.class);
                routingTable.insert(new Node(new KademliaId(DigestUtil.Sha1AndSha256(publicKey)), connection.getIpv6(),
                        Integer.valueOf(connection.getPort()), publicKey, AES));
                ctx.attr(AttributeKey.valueOf("isSuccess")).set(true);
                ctx.close();
            }
        }else {
            System.out.println("HandShake类型异常");
            ctx.close();
        }
    }
}
