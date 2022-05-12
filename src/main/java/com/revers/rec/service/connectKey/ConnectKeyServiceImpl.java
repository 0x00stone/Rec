package com.revers.rec.service.connectKey;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.ConnectKey;
import com.revers.rec.mapper.ConnectKeyMapper;
import com.revers.rec.service.connectKey.ConnectKeyService;
import com.revers.rec.util.cypher.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ConnectKeyServiceImpl implements ConnectKeyService {
    @Autowired
    private ConnectKeyMapper connectKeyMapper;

    @Override
    public ConnectKey getConnectKey(String id) {
        return connectKeyMapper.findConnectKeyById(id, DigestUtil.Sha1AndSha256(AccountConfig.getPublicKey()));
    }

    @Override
    public void deleteConnectKeyById(String id) {
        connectKeyMapper.deleteConnectKeyById(id, DigestUtil.Sha1AndSha256(AccountConfig.getPublicKey()));
    }

    @Override
    public List<ConnectKey> findAllConnectKey() {
        return connectKeyMapper.findAllConnectKey();
    }

    @Override
    public void saveConnectKey(ConnectKey connectKey) {
        if(connectKey.getId1() == null||connectKey.getId2() == null){
            log.info("id1或id2值为空");
            return;
        }
        if(getConnectKey(connectKey.getId1()) == null) {
            connectKeyMapper.insertConnectKey(connectKey);
        }else {
            log.info("已存在同id字段");
        }
    }

    @Override
    public void updateConnectKey(ConnectKey connectKey) {
        if(connectKey.getId1() == null||connectKey.getId2()== null){
            log.info("id1或id2值为空");
            return;
        }
        connectKeyMapper.updateConnectKey(connectKey);
    }
}
