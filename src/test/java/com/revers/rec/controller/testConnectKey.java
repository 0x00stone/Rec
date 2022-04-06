package com.revers.rec.controller;

import com.revers.rec.RecApplication;
import com.revers.rec.domain.ConnectKey;
import com.revers.rec.service.ConnectKeyService;
import com.revers.rec.service.ConnectKeyServiceImpl;
import org.checkerframework.checker.units.qual.C;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecApplication.class)
public class testConnectKey {

    @Autowired
    private ConnectKeyServiceImpl connectKeyService;

    @Test
    public void testConnectKey() {
        ConnectKey key = new ConnectKey();
        key.setAesKey("aes");
        key.setPublicKey("publicKey");
        key.setOrder(1);
        key.setId("id");
        key.setTimeStamp(System.currentTimeMillis());
        ;
        if(connectKeyService.getConnectKey(key.getId()) == null) {
            connectKeyService.saveConnectKey(key);
        }
        System.out.println(connectKeyService.getConnectKey("id").toString());
        key.setAesKey("aes2");
        connectKeyService.updateConnectKey(key);

        ConnectKey key2 = new ConnectKey();
        key2.setAesKey("aes3");
        key2.setPublicKey("publicKey3");
        key2.setOrder(3);
        key2.setId("id3");
        key2.setTimeStamp(System.currentTimeMillis());

        if(connectKeyService.getConnectKey(key2.getId()) == null) {
            connectKeyService.saveConnectKey(key2);
        }
        for(ConnectKey temp : connectKeyService.findAllConnectKey()) {
            System.out.println(temp.toString());
        }
    }
}
