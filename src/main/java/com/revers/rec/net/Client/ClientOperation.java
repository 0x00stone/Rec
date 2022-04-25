package com.revers.rec.net.Client;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.Data;
import com.revers.rec.net.Client.communicate.ClientCommunicate;
import com.revers.rec.net.Client.handShake.HandShakeClient;
import com.revers.rec.net.Client.ping.ClientPing;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.ResultUtil;
import com.revers.rec.util.cypher.RsaUtil;
import com.revers.rec.util.cypher.Sha256Util;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.lang.invoke.ConstantBootstraps;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j
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

    public static ResultUtil communicate(String destPublicKey,String content) throws ExecutionException, InterruptedException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Data data = new Data();
        data.setSrcPublicKey(RsaUtil.publicEncrypt(AccountConfig.getPublicKey(),destPublicKey));
        data.setDestPublicKey(destPublicKey);
        data.setData(RsaUtil.publicEncrypt(content,destPublicKey));
        data.setSignature(RsaUtil.privateEncrypt(Sha256Util.getSHA256(data.getData()),AccountConfig.getPrivateKey()));
        data.setTimeStamp(String.valueOf(System.currentTimeMillis()));

        Data communicate = communicate(data);
        //TODO: check signature

        if(communicate != null){
            if(AccountConfig.getPublicKey().equals(communicate.getDestPublicKey()) ){
                String dataCommunicate = RsaUtil.privateDecrypt(communicate.getData(),AccountConfig.getPrivateKey());
                if(ConstantUtil.COMMUNICATE_SUCCESS.equals(dataCommunicate)){
                    communicate.setData(dataCommunicate);
                    return new ResultUtil(true,null,communicate);
                }
            }
        }
        log.info("对方未成功接收");
        return new ResultUtil(false,null,communicate);
    }

    public static Data communicate(Data data) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, ExecutionException, InterruptedException {
        FutureTask<Data> futureTask = new FutureTask<Data>(new ClientCommunicate(data));
        futureTask.run();
        return futureTask.get();
    }

}
