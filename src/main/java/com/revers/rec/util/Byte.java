package com.revers.rec.util;

import java.io.*;

public class Byte {
    /**
     * 对象转字节数组
     */
    public static byte[] objectToBytes(Object obj) throws IOException {
        try(
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectOutputStream sOut = new ObjectOutputStream(out);
        ){
            sOut.writeObject(obj);
            sOut.flush();
            byte[] bytes = out.toByteArray();
            return bytes;
        }
    }

    /**
     * 字节数组转对象
     */
    public static Object bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        try(
                ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                ObjectInputStream sIn = new ObjectInputStream(in);
        ){
            return sIn.readObject();
        }
    }
}
