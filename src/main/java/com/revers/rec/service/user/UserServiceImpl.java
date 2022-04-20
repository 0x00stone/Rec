package com.revers.rec.service.user;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.User;
import com.revers.rec.mapper.UserMapper;
import com.revers.rec.util.ResultUtil;
import com.revers.rec.util.cypher.RsaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static com.revers.rec.util.cypher.AesUtil.getAseKey;
import static com.revers.rec.util.cypher.Sha256Util.getSHA256;
import static com.revers.rec.util.cypher.VigenereUtil.deVigenere;
import static com.revers.rec.util.cypher.VigenereUtil.enVigenere;

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
    public ResultUtil login(String username, String password) {
        log.info("Service: " + username + "登录");
        ResultUtil resultUtil = new ResultUtil();

        User user = userMapper.findUserByUsername(username);
        if (user == null) {
            resultUtil.setFlag(false);
            resultUtil.setMsg("数据库中无 username 为 " + username + " 的用户");
            log.info("数据库中无 username 为 " + username + " 的用户");
        } else {
            accountConfig.setId(user.getId());
            accountConfig.setUsername(user.getUsername());
            accountConfig.setPublicKey(user.getPublicKey());
            accountConfig.setPrivateKey(user.getPrivateKey());
            accountConfig.setStatus(user.isStatus());
            accountConfig.setAeskey(deVigenere(user.getAeskey(), getSHA256(password)));
            resultUtil.setFlag(true);
            resultUtil.setMsg("用户: " + username + " 已登录");
            resultUtil.setData(user);
            log.info("用户: " + username + " 已登录");
            log.info("用户信息 : " + accountConfig);
        }
        return resultUtil;
    }



    @Transactional
    @Override
    public ResultUtil register(String username, String password) throws NoSuchAlgorithmException {
        log.info("Service: " + username + "注册");
        ResultUtil resultUtil = new ResultUtil();
        if (userMapper.findUserByUsername(username) != null ){
            resultUtil.setFlag(false);
            resultUtil.setMsg(username + " 用户已存在");
            log.info(username + " 用户已存在");
        }else {
            Map<String, String> rsaKey = RsaUtil.createKeys(4096);
            user.setPublicKey(rsaKey.get("publicKey"));
            user.setPrivateKey(rsaKey.get("privateKey"));
            user.setId(DigestUtils.sha1Hex(rsaKey.get("publicKey")));
            user.setUsername(username);
            String originkey = getAseKey(256);
            String aesKey = enVigenere(originkey,getSHA256(password));
            user.setAeskey(aesKey);
            System.out.println(userMapper.createUser(user));

            resultUtil.setFlag(true);
            resultUtil.setMsg(username + " 注册成功");
            log.info(username + " 注册成功");
        }
        return resultUtil;
    }
}
