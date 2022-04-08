package com.revers.rec.others;

import com.revers.rec.domain.Data;
import org.junit.Test;

import java.io.*;
import java.nio.channels.Pipe;

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
}
