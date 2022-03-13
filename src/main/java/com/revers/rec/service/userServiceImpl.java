package com.revers.rec.service;

import com.revers.rec.config.accountConfig;
import com.revers.rec.domain.user;
import com.revers.rec.mapper.userMapper;
import com.revers.rec.util.cypher.rsa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import static com.revers.rec.util.cypher.aes.getAseKey;
import static com.revers.rec.util.cypher.rsa.privateEncrypt;
import static com.revers.rec.util.cypher.sha256.getSHA256;
import static com.revers.rec.util.cypher.vigenere.deVigenere;
import static com.revers.rec.util.cypher.vigenere.enVigenere;

@Service
public class userServiceImpl implements userService{
    @Autowired
    private user user;

    @Autowired
    private userMapper userMapper;

    @Autowired
    private accountConfig accountConfig;

    // 从 数据库中查找 username 用户并放入accountConfig账户配置中
    @Override
    public boolean login(String username,String password) {
        user user = userMapper.selectUser(username);
        if(user == null) {
            System.out.println("无 username 为 "+ username + " 的用户");
            return false;
        }else {
            accountConfig.setId(user.getId());
            accountConfig.setUsername(user.getUsername());
            accountConfig.setPublicKey(user.getPublicKey());
            accountConfig.setPrivateKey(user.getPrivateKey());
            accountConfig.setNodeId(user.getNodeId());
            accountConfig.setSign(user.getSign());
            accountConfig.setOnlineTime(user.getOnlineTime());
            accountConfig.setIpv6Port(user.getIpv6Port());
            accountConfig.setIpv6(user.getIpv6());
            accountConfig.setIpv4Port(user.getIpv4Port());
            accountConfig.setIpv4(user.getIpv4());
            accountConfig.setStatus(user.isStatus());
            accountConfig.setAeskey(deVigenere(user.getAeskey(),password));
            System.out.println("发现用户 :" + username);
            System.out.println(accountConfig);
            return true;
        }
    }

    @Override
    public void createUser(String username,String password) throws NoSuchAlgorithmException {
        Map<String, String> rsaKey = rsa.createKeys(4096);
        user.setPublicKey(rsaKey.get("publicKey"));
        user.setPrivateKey(rsaKey.get("privateKey"));

        user.setId(getSHA256(rsaKey.get("publicKey")));
        user.setUsername(username);

        String sign = new String(username.getBytes(StandardCharsets.UTF_8));

        user.setSign( privateEncrypt(sign,rsaKey.get("privateKey"))); // 签名

        String originkey = getAseKey(256);
        String aesKey = enVigenere(originkey,getSHA256(password));
        user.setAeskey(aesKey);
        System.out.println("origin : " + originkey);
        System.out.println("en : " + aesKey);


        userMapper.createUser(user);
        System.out.println(user);
    }
}
