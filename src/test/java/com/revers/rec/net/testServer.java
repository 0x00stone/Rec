package com.revers.rec.net;

import com.revers.rec.RecApplication;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.net.Server.Server;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.*;

import static com.revers.rec.util.cypher.RsaUtil.createKeys;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecApplication.class)
public class testServer {

    @Test
    public void testServer() throws ExecutionException, InterruptedException {
        Map<String, String> keyMap = createKeys(4096);
        String publicKey = keyMap.get("publicKey");
        String privateKey = keyMap.get("privateKey");
        AccountConfig.setPublicKey(publicKey);
        AccountConfig.setPrivateKey(privateKey);
        AccountConfig.setIpv6("127.0.0.2");
        AccountConfig.setIpv6Port(30000);

       FutureTask<Boolean> futureTask = new FutureTask<>(new Server());
       futureTask.run();
       if(futureTask.get() == false){}
    }


}
