package com.revers.rec.net;

import com.revers.rec.domain.Connection;
import com.revers.rec.domain.Data;
import com.revers.rec.net.Client.Client;
import org.junit.Test;

public class testClient {

    @Test
    public void testClient(){
        Data data  = new Data();
        data.setData("test");

        Connection connection = new Connection();
        connection.setDestId("destId");
        connection.setSrcId("srcId");
        connection.setDestPublicKey("destPublicKey");
        connection.setData(data);
        connection.setSrcPublicKey("srcPublicKey");

        Thread client = new Thread(new Client("127.0.0.1",7686,connection));
        client.start();
        while (true){

        }
    }
}
