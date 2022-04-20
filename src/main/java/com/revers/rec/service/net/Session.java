package com.revers.rec.service.net;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class Session {
    public static HashMap<String,Object[]> handShakeSession = new HashMap<>(); //<id,[type,publicKey,AESKEY,order]>

}
