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
        AccountConfig.setId(DigestUtil.Sha1AndSha256(publicKey));
        AccountConfig.setPublicKey(publicKey);
        AccountConfig.setPrivateKey(privateKey);
        AccountConfig.setIpv6("127.0.0.2");
        AccountConfig.setIpv6Port(30000);
        ResultUtil handshake = ClientOperation.handShake("127.0.0.1",30000);

        String destPublicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA7zKjCaBLMcBLba_ra_6HsF3gJ9p67NZGjjmTLtvysWVWjpLgX0WVA3YbaAeowVA9wWolLXvzxieZYe0iSPmhs1Qn3Xe_9AeGlr9OggpOkLi5xHj7IHn8cLiqdHBl-SX7jGCROtM0Z0TERrNA6s8MaBoXEPcx3XIL6hYJSHCe0YJtA75ILhql4jNw0pTqHXRJrIoNSg8jN9i34DNf1p83PR9LpXw_Nm5rdz__uqlEMFV2wjNtRfy9LpxHbJ9OGHqD9HOH4djrYc18IOtPFpIEWcO4F3e9u4Rf5tfxTIsqdsUy52KiUEFdYECB8eR6ap_BsItxM0NypTFXSpcdPGvVIuFxiprCfqHowmcQax4wFMYcZm3yG-saTvAWWJzUvFvyuluJlkb0FPHXkAx26H_zSB1gQ1eWRPDmNFQGfmzY3nvnCkyJvJMswbzUwTsspK3jR_5BrzBaeF4KNYzdbOLDD90wjj8lm7PPW65E2_rD-DPq3kMV_g0EToj9UgjgQu9G6MrU4Ji4okhNZpOVQM0fk0awJL8rhHBCw7NchKva1obm1AIAoH8ZlTOJkH6YAupMqlLv-oduQUY20Kws20pOOy7-BMUkZxarpERl-LpDMZACj46_yQsJylRM5MraYSd6afvFkgyedoxnulFtWUQqoSzca4lPgjyMgqPQVNT0oJMCAwEAAQ";
        ResultUtil communicate = ClientOperation.communicate(destPublicKey,"test");
        System.out.println(communicate.getFlag());
        System.out.println(communicate.getMsg());
        System.out.println(communicate.getData());
    }

}
