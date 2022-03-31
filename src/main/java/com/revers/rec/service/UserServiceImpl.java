package com.revers.rec.service;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.User;
import com.revers.rec.mapper.UserMapper;
import com.revers.rec.util.Result;
import com.revers.rec.util.cypher.Rsa;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.MGF1ParameterSpec;
import java.util.Map;

import static com.revers.rec.util.cypher.Aes.getAseKey;
import static com.revers.rec.util.cypher.Rsa.privateEncrypt;
import static com.revers.rec.util.cypher.Sha256.getSHA256;
import static com.revers.rec.util.cypher.Vigenere.deVigenere;
import static com.revers.rec.util.cypher.Vigenere.enVigenere;
import static java.security.spec.MGF1ParameterSpec.SHA1;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private User user;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountConfig accountConfig;

    // 从 数据库中查找 username 用户并放入accountConfig账户配置中
    @Override
    public Result login(String username,String password) {
        log.info("Service: " + username + "登录");
        Result result = new Result();

        User user = userMapper.findUserByUsername(username);
        if (user == null) {
            result.setFlag(false);
            result.setMsg("数据库中无 username 为 " + username + " 的用户");
            log.info("数据库中无 username 为 " + username + " 的用户");
        } else {
            accountConfig.setId(user.getId());
            accountConfig.setUsername(user.getUsername());
            accountConfig.setPublicKey(user.getPublicKey());
            accountConfig.setPrivateKey(user.getPrivateKey());
            accountConfig.setStatus(user.isStatus());
            accountConfig.setAeskey(deVigenere(user.getAeskey(), getSHA256(password)));
            result.setFlag(true);
            result.setMsg("用户: " + username + " 已登录");
            result.setData(user);
            log.info("用户: " + username + " 已登录");
            log.info("用户信息 : " + accountConfig);
        }
        return result;
    }



    @Transactional
    @Override
    public Result register(String username,String password) throws NoSuchAlgorithmException {
        log.info("Service: " + username + "注册");
        Result result = new Result();
        if (userMapper.findUserByUsername(username) != null ){
            result.setFlag(false);
            result.setMsg(username + " 用户已存在");
            log.info(username + " 用户已存在");
        }else {
            Map<String, String> rsaKey = Rsa.createKeys(4096);
            user.setPublicKey(rsaKey.get("publicKey"));
            user.setPrivateKey(rsaKey.get("privateKey"));
            user.setId(DigestUtils.sha1Hex(rsaKey.get("publicKey")));
            user.setUsername(username);
            String originkey = getAseKey(256);
            String aesKey = enVigenere(originkey,getSHA256(password));
            user.setAeskey(aesKey);
            System.out.println(userMapper.createUser(user));

            result.setFlag(true);
            result.setMsg(username + " 注册成功");
            log.info(username + " 注册成功");
        }
        return result;
    }
}
