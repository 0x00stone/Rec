package com.revers.rec.util.cypher;

public class DigestUtil {
    public static String Sha1AndSha256(String data){
        return Sha1Util.sha1(Sha256Util.getSHA256(data));
    }
}
