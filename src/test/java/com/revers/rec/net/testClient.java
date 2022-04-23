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
        String destPublicKey ="MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAzk94fzt5Iyhm2XlNeqzqaQM27_yyBQgpC2TaP42YLbfKJlOf19oipY17JjWCLdH01iFForNSgGxEqwjt7IOQox7GGJSjGJuVaZZAe6NHsy2mivtmZ7-o90dAozUWq0h3_q3PVlq2-jTgvW_yZHgkOimG2iI7MyWTYbQGvS-q9ClMbJIyGTaCKh_hokUvDqYz-3uVDVAhw6MgiA1Gv_f7MJEQs7tDJoonlDSaep6apOy9bjDWtA68ERcGcMgGpq2dMFSR3vWT8AsPFdql3A2e6mcZ5zTi-39gvRcw3DLmfV3T_kWRr2cTZ7iiQixyKkZQcEFMahpT9LYTYemZlWxt3Z02PavoycruZWJ25Q6UL6uvbSxQZy-mZ9YfTUrbUeGztiy_Nh0riRJTGjqSHI7WZsT6ySWFHmfkKCw6etKlj6ulczkAH2J1Oy7zctP6dxstUZh9W2KFaAJLTgMb8Stre84dDseRgTDup8QJSFL7m0k9ZBMqkONG6nl9UhcAOVQjYjGpk-bOPnTaeV5PwE2xMJGWrdnPnaGuWRGs78a0U0MxsjsQ3tnz2qf_c2c9iRhcGJCNdaqkoIieAC0IW4ddRxQhZmgjx1_6TdYt0SKY5lDqKqt3VnPj2945ZcfSX4VLzk7Slr0ZuhQVbIXxAblONg0pibS8O43OTY4V__pCZL0CAwEAAQ";
        ResultUtil communicate = ClientOperation.communicate(destPublicKey,"test");
        System.out.println(communicate.getFlag());
        System.out.println(communicate.getMsg());
        System.out.println(communicate.getData());
    }

}
