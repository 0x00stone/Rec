package com.revers.rec.service.connectKey;

import com.revers.rec.domain.ConnectKey;
import com.revers.rec.mapper.ConnectKeyMapper;
import com.revers.rec.service.connectKey.ConnectKeyService;
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
        return connectKeyMapper.findConnectKeyById(id);
    }

    @Override
    public void deleteConnectKeyById(String id) {
        connectKeyMapper.deleteConnectKeyById(id);
    }

    @Override
    public List<ConnectKey> findAllConnectKey() {
        return connectKeyMapper.findAllConnectKey();
    }

    @Override
    public void saveConnectKey(ConnectKey connectKey) {
        if(connectKey.getId() == null){
            log.info("id值为空");
            return;
        }
        if(getConnectKey(connectKey.getId()) == null) {
            connectKeyMapper.insertConnectKey(connectKey);
        }else {
            log.info("已存在同id字段");
        }
    }

    @Override
    public void updateConnectKey(ConnectKey connectKey) {
        if(connectKey.getId() == null){
            log.info("id值为空");
            return;
        }
        connectKeyMapper.updateConnectKey(connectKey);
    }
}
