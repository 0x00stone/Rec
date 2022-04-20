package com.revers.rec.net;

import com.revers.rec.Kademlia.Bucket.RoutingTable;
import com.revers.rec.RecApplication;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.net.Client.ClientOperation;
import com.revers.rec.util.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.revers.rec.util.cypher.Rsa.createKeys;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecApplication.class)
public class testClient {
    @Autowired
    RoutingTable routingTable;

    @Test
    public void testPing() throws ExecutionException, InterruptedException {
        Result ping = ClientOperation.ping("127.0.0.1", 30000);
        System.out.println(ping.getFlag());
        System.out.println(ping.getMsg());
        System.out.println(ping.getData());
    }

    @Test
    public void testHandShake() throws NoSuchAlgorithmException, ExecutionException, InterruptedException {

        Map<String, String> keyMap = createKeys(4096);
        String publicKey = keyMap.get("publicKey");
        String privateKey = keyMap.get("privateKey");
        AccountConfig.setPublicKey(publicKey);
        AccountConfig.setPrivateKey(privateKey);
        AccountConfig.setIpv6("127.0.0.2");
        AccountConfig.setIpv6Port(30000);

        Result handshake = ClientOperation.handShake("127.0.0.1",30000);
        System.out.println(handshake.getFlag());
        System.out.println(handshake.getMsg());
        System.out.println(handshake.getData());

    }

}
