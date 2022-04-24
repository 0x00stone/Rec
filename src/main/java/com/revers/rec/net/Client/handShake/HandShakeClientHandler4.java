package com.revers.rec.net.Client.handShake;

import com.revers.rec.Kademlia.Bucket.RoutingTable;
import com.revers.rec.Kademlia.Node.KademliaId;
import com.revers.rec.Kademlia.Node.Node;
import com.revers.rec.domain.ConnectKey;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.service.connectKey.ConnectKeyService;
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
    @Autowired
    private ConnectKeyService connectKeyService;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HashMap<String, Object> map = (HashMap<String, Object>) msg;
        MsgProtobuf.Connection connection = (MsgProtobuf.Connection) map.get("connection");


        if(connection.getMsgType() == ConstantUtil.MSGTYPE_HANDSHAKE_4){
            String publicKey = (String)ctx.attr(AttributeKey.valueOf("publicKey")).get();
            String id = DigestUtil.Sha1AndSha256(publicKey);
            String AES = (String)ctx.attr(AttributeKey.valueOf("aes")).get();

            if("success".equals(AesUtil.decrypt(AES,connection.getData()))) {
                this.routingTable = BeanContextUtil.getBean(RoutingTable.class);
                this.connectKeyService = BeanContextUtil.getBean(ConnectKeyService.class);

                routingTable.insert(new Node(new KademliaId(DigestUtil.Sha1AndSha256(publicKey)), connection.getIpv6(),
                        Integer.valueOf(connection.getPort()), publicKey, AES));

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

                ctx.attr(AttributeKey.valueOf("isSuccess")).set(true);
                ctx.close();
            }
        }else {
            System.out.println("HandShake类型异常");
            ctx.close();
        }
    }
}
