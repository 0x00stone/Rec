package com.revers.rec.controller.Client;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.Data;
import com.revers.rec.domain.protobuf.MsgProtobuf;
import com.revers.rec.util.Result;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ClientOperation {
    public static Result ping(String ip, int port) throws ExecutionException, InterruptedException {
        FutureTask<Result> futureTask = new FutureTask<Result>(new ClientPing(ip,port));
        futureTask.run();
        return futureTask.get();
    }

    public static Result client(String ip, int port, String destId,String destPublicKey,Data data) throws ExecutionException, InterruptedException {
        MsgProtobuf.connection connection = MsgProtobuf.connection.newBuilder()
                .setData(data.getData())
                .setSrcId(AccountConfig.getId())
                .setDestId(destId)
                .setSrcPublicKey(AccountConfig.getPublicKey())
                .setDestPublicKey(destPublicKey)
                .build();


        FutureTask<Result> futureTask = new FutureTask<Result>(new Client(ip,port,connection));
        futureTask.run();
        return futureTask.get();
    }
}
