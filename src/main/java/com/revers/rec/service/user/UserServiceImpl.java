package com.revers.rec.service.user;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.User;
import com.revers.rec.mapper.UserMapper;
import com.revers.rec.service.table.MessageTableService;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.Result;
import com.revers.rec.util.cypher.DigestUtil;
import com.revers.rec.util.cypher.RsaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private MessageTableService messageTableService;

    // 从 数据库中查找 username 用户并放入accountConfig账户配置中
    @Override
    public Result login(String username, String password) {
        Result result = new Result();

        User user = userMapper.findUserByUsername(username);
        if (user == null) {
            result.setFlag(ConstantUtil.ERROR);
            result.setMsg("数据库中无 username 为 " + username + " 的用户");
            log.info("数据库中无 username 为 " + username + " 的用户");
        } else {
            accountConfig.setId(user.getId());
            accountConfig.setUsername(user.getUsername());
            accountConfig.setPublicKey(user.getPublicKey());
            accountConfig.setPrivateKey(user.getPrivateKey());
            accountConfig.setStatus(user.getStatus());
            accountConfig.setAeskey(deVigenere(user.getAeskey(), getSHA256(password)));
            accountConfig.setSign(user.getSign());
            accountConfig.setPortrait(user.getPortrait());
            result.setFlag(ConstantUtil.SUCCESS);
            result.setMsg("用户: " + username + " 已登录");
            result.setData(user);
            log.info("用户: " + username + " 已登录");
            log.info("用户信息 : " + accountConfig);
        }
        return result;
    }

    @Override
    public Result register(String username, String password) throws NoSuchAlgorithmException {
        Result result = new Result();
        if (userMapper.findUserByUsername(username) != null ){
            result.setFlag(ConstantUtil.ERROR);
            result.setMsg(username + " 用户已存在");
            log.info(username + " 用户已存在");
        }else {
            Map<String, String> rsaKey = RsaUtil.createKeys(4096);
            user.setPublicKey(rsaKey.get("publicKey"));
            user.setPrivateKey(rsaKey.get("privateKey"));
            user.setId(DigestUtil.Sha1AndSha256(rsaKey.get("publicKey")));
            user.setUsername(username);
            String originkey = getAseKey(256);
            String aesKey = enVigenere(originkey,getSHA256(password));
            user.setAeskey(aesKey);
            messageTableService.createTable(user.getId());
            System.out.println(userMapper.createUser(user));


            result.setFlag(ConstantUtil.SUCCESS);
            result.setMsg(username + " 注册成功");
        }
        return result;
    }


}
