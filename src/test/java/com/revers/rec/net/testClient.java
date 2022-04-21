package com.revers.rec.net;

import com.revers.rec.Kademlia.Bucket.RoutingTable;
import com.revers.rec.RecApplication;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.net.Client.ClientOperation;
import com.revers.rec.util.ResultUtil;
import com.revers.rec.util.cypher.DigestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.revers.rec.util.cypher.RsaUtil.createKeys;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecApplication.class)
public class testClient {
    @Autowired
    RoutingTable routingTable;

    @Test
    public void testPing() throws ExecutionException, InterruptedException {
        ResultUtil ping = ClientOperation.ping("127.0.0.1", 30000);
        System.out.println(ping.getFlag());
        System.out.println(ping.getMsg());
        System.out.println(ping.getData());
    }

    @Test
    public void testHandShake() throws NoSuchAlgorithmException, ExecutionException, InterruptedException {

        Map<String, String> keyMap = createKeys(4096);
        String publicKey = keyMap.get("publicKey");
        String privateKey = keyMap.get("privateKey");
        System.out.println(publicKey);
        System.out.println(privateKey);
        AccountConfig.setPublicKey(publicKey);
        AccountConfig.setPrivateKey(privateKey);
        AccountConfig.setIpv6("127.0.0.2");
        AccountConfig.setIpv6Port(30000);

        ResultUtil handshake = ClientOperation.handShake("127.0.0.1",30000);
        System.out.println(handshake.getFlag());
        System.out.println(handshake.getMsg());
        System.out.println(handshake.getData());
    }
    @Test
    public void testCommunication() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, ExecutionException, InvalidKeyException, InterruptedException {
        Map<String, String> keyMap = createKeys(4096);
        String publicKey = keyMap.get("publicKey");
        String privateKey = keyMap.get("privateKey");
        System.out.println(publicKey);
        System.out.println(privateKey);
        AccountConfig.setId(DigestUtil.Sha1AndSha256(publicKey));
        AccountConfig.setPublicKey(publicKey);
        AccountConfig.setPrivateKey(privateKey);
        AccountConfig.setIpv6("127.0.0.2");
        AccountConfig.setIpv6Port(30000);
        ResultUtil handshake = ClientOperation.handShake("127.0.0.1",30000);

        String destPublicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAvdYTFZkfztFrJVnHjqiEwsLelPkO6JS7z3Wj3Oi9duXG375gWaoaEgbVpTcuxI032pmLCwL_FeV72djtzjkgLdrU7qXuhxC6n9SN3fKW6ufWkpMgKZ09XrcOZxdCpAC-Abp7lhZY5PyCq52ITOTb5glaL8vFh-iXWdV1DqkrQABwZ45fwNW_ReKykxIzIBfgalK53w1ZWPfqyNtQlOBreLRSv5zmSvT4Fuwj_HzQIyAnc_EsEVtwEh-hM--hsnxy8nFEiFSmkYaisd4LO1prwjAiWpM1A4xXk74Yq7hdEhA6U3kseC5G32yVAX7C0Ccw47MB4t7Fz8GJzWSMDLIuUXsRs3tmNboLhPZTxOdzSgF-eYsuCPvYEwGfXt9_sDv2Hd92XinMh6Mp-MSsV7eM8NN57FzM2J7u4mkaL6T_d51VMBgjZaa_Ct0rXs3YAGkdz_xdfYNTYClcLuO06hnmZ_f-rcWnjMJTJwRl3n2NAWPRyXk5xpB8J83fw24J0TalwxajR4yteMOpEL-7lR0jNxt1GAvT0sEhE3hfvF-2AJ0o8A42Gj0QlIlB-E_r7I3OB6K7L86AIcuNPSF21XJm6HyCL_fJqbCvJkLMwAnBg7G5ExNI4f_zehNG5AJcxGawhF5d6LENHD_fzGsytvBY6Yf7JuImiQfPSSU52YlNv0MCAwEAAQ";
        ResultUtil communicate = ClientOperation.communicate(destPublicKey,"test");
        System.out.println(communicate.getFlag());
        System.out.println(communicate.getMsg());
        System.out.println(communicate.getData());
    }

}
