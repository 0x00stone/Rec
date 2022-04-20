package com.revers.rec.controller.net;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.Data;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.controller.Client.Client;
import com.revers.rec.controller.Client.ClientOperation;
import com.revers.rec.controller.Client.ClientPing;
import com.revers.rec.util.Result;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class testClient {

    @Test
    public void testClient() throws ExecutionException, InterruptedException {
        Data data  = new Data();
        data.setData("test");

        MsgProtobuf.connection connection = MsgProtobuf.connection.newBuilder()
                .setData(data.getData())
                .setSrcId("srcId2")
                .setDestId("destId")
                .setSrcPublicKey("srcPublicKey")
                .setDestPublicKey("destPublicKey")
                .build();


        FutureTask<Result> futureTask = new FutureTask<Result>(new Client("127.0.0.1",30000,connection));
        futureTask.run();
        while (futureTask.get().getFlag() != false){}
    }

    @Test
    public void testPing() throws ExecutionException, InterruptedException {

        AccountConfig.setId("id123");
        AccountConfig.setPublicKey("publicKey123");
        Result ping = ClientOperation.ping("127.0.0.1", 30000);
        System.out.println(ping.getFlag());
        System.out.println(ping.getMsg());
        System.out.println(ping.getData());

    }
}
