package com.revers.rec.net.Client;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.Data;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.net.Client.handShake.HandShakeClient;
import com.revers.rec.net.Client.ping.ClientPing;
import com.revers.rec.util.ResultUtil;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ClientOperation {
    public static ResultUtil ping(String ip, int port) throws ExecutionException, InterruptedException {
        FutureTask<ResultUtil> futureTask = new FutureTask<ResultUtil>(new ClientPing(ip,port));
        futureTask.run();
        return futureTask.get();
    }

    public static ResultUtil handShake(String ip, int port) throws NoSuchAlgorithmException, ExecutionException, InterruptedException {
        FutureTask<ResultUtil> futureTask = new FutureTask<ResultUtil>(new HandShakeClient(ip,port));
        futureTask.run();
        return futureTask.get();
    }

    public static ResultUtil client(String ip, int port, String destId, String destPublicKey, Data data) throws ExecutionException, InterruptedException {
        MsgProtobuf.Connection connection = MsgProtobuf.Connection.newBuilder()
                .setData(data.getData())
                .setSrcPublicKey(AccountConfig.getPublicKey())
                .setDestPublicKey(destPublicKey)
                .build();


        FutureTask<ResultUtil> futureTask = new FutureTask<ResultUtil>(new Client(ip,port,connection));
        futureTask.run();
        return futureTask.get();
    }
}
