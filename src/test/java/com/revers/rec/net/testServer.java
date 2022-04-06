package com.revers.rec.net;

import com.revers.rec.net.Server.Server;
import org.junit.Test;

public class testServer {

    @Test
    public void testServer() {
        new Thread(new Server()).start();
        while (true) {}
    }
}
