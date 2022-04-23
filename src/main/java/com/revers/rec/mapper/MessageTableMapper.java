package com.revers.rec.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MessageTableMapper {

    @Update("CREATE TABLE '${tableName}' (  messageId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " +
            "type integer,  isSender integer,  isRead integer,  strTalker TEXT,  " +
            "strContent TEXT,  createTime integer,  updateTime integer);")
    void createTable(String tableName);
}
