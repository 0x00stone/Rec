package com.revers.rec.service;

import com.revers.rec.domain.ConnectKey;

import java.util.List;

public interface ConnectKeyService {
    public ConnectKey getConnectKey(String id);

    public void deleteConnectKeyById(String id);

    public List<ConnectKey> findAllConnectKey();

    public void saveConnectKey(ConnectKey connectKey);

    public void updateConnectKey(ConnectKey connectKey);

}
