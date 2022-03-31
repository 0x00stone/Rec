package com.revers.rec;

import com.revers.rec.net.Client.Client;
import com.revers.rec.net.Server.Server;
import org.junit.jupiter.api.Test;

public class testNet {
    @Test
    public void testServer() throws InterruptedException {
        new Thread(new Server()).start();
        while (true){

        }
    }

    @Test
    void testClient() throws InterruptedException {
        new Thread(new Client()).start();
        while (true){}
    }
}
