package com.revers.rec.util;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import static com.revers.rec.util.cypher.RsaUtil.*;

public class testRsaUtil {
    @Test
    public void testRsa() throws NoSuchAlgorithmException, InvalidKeySpecException {
        Map<String, String> keyMap = createKeys(4096);
        String publicKey = keyMap.get("publicKey");
        String privateKey = keyMap.get("privateKey");
        System.out.println("公钥: \n\r" + publicKey);
        System.out.println("私钥： \n\r" + privateKey);

        System.out.println("公钥加密——私钥解密");
        String str = "abc";
        System.out.println("\r明文：" + str);
        String encodedData = privateEncrypt(str, privateKey);  //传入明文和公钥加密,得到密文
        String decodedData = publicDecrypt(encodedData, publicKey); //传入密文和私钥,得到明文
        System.out.println("解密后文字: " + decodedData);



    }
}
