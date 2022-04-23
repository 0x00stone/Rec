package com.revers.rec.service.table;

import com.revers.rec.mapper.MessageTableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageTableServiceImpl implements MessageTableService {
    @Autowired
    private MessageTableMapper messageTableMapper;

    @Override
    public void createTable(String tableName) {
        messageTableMapper.createTable(tableName);
    }
}
