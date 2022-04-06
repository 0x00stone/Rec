package com.revers.rec.net;

import com.revers.rec.net.Client.Client;
import org.junit.Test;

public class testClient {

    @Test
    public void testClient(){
        Thread client = new Thread(new Client("127.0.0.1",7686));
        client.start();
        while (true){

        }
    }
}
