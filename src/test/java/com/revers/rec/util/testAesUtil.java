package com.revers.rec.util;

import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.revers.rec.util.cypher.AesUtil.*;
import static com.revers.rec.util.cypher.Sha256Util.getSHA256;
import static com.revers.rec.util.cypher.VigenereUtil.deVigenere;
import static com.revers.rec.util.cypher.VigenereUtil.enVigenere;

public class testAesUtil {

    @Test
    public void testAes() throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String string = "helloworld";
        String aes = getAseKey(256);

        String jiami = encrypt(aes,string);

        String password = "123456";
        System.out.println("密码:" + password);

        String password_256 = getSHA256(password);
        System.out.println("sha256:" + password_256);

        String aes_vi = enVigenere(aes,password_256);
        System.out.println("加密后aes:" + aes_vi);
        System.out.println("解密后aes:" + deVigenere(aes_vi,password_256));
        System.out.println("aes     :" + aes);

        String password2 = "123456";
        String password2_256 = getSHA256(password2);
        String aes_devi = deVigenere(aes_vi,password2_256);

        System.out.println(jiami);
        System.out.println(decrypt(aes_devi,jiami));


    }
}
