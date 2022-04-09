package com.revers.rec.others;

import com.alibaba.fastjson.JSON;
import com.revers.rec.domain.Data;
import com.revers.rec.util.cypher.Rsa;
import org.junit.Test;

import java.io.*;
import java.nio.channels.Pipe;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.revers.rec.util.cypher.Rsa.*;

public class testSerializable {
    @Test
    public void testData() throws IOException, ClassNotFoundException {
        Data data = new Data();
        data.setData("data");
        data.setDestId("test");
        data.setSrcId("test");
        data.setMsgType(1);


        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        new ObjectOutputStream(bos).writeObject(data);

        System.out.println("读取数据");
        ByteArrayInputStream in = new ByteArrayInputStream(bos.toByteArray());
        Data data1 = (Data) new ObjectInputStream(new BufferedInputStream(in)).readObject();
        System.out.println("data数据为:" + data1.getData());
    }

    @Test
    public void testDataJSON(){
        Map<String, String> keyMap = createKeys(4096);
        String publicKey = keyMap.get("publicKey");
        String privateKey = keyMap.get("privateKey");

        Data data = new Data();
        data.setData("data");
        data.setDestId("test");
        data.setSrcId("test");
        data.setMsgType(1);

        String json = JSON.toJSONString(data);
        System.out.println(json);

        String encrypt = Rsa.publicEncrypt(json, publicKey);
        System.out.println(encrypt);

        String s = privateDecrypt(encrypt, privateKey);
        System.out.println(s);

        Data data1 = JSON.parseObject(s, Data.class);
        System.out.println(data1.toString());


    }
}
