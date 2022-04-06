package com.revers.rec.mapper;

import com.revers.rec.domain.ConnectKey;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConnectKeyMapper {

    @Delete("DELETE FROM key WHERE id = #{id}")
    public void deleteConnectKeyById(String id);

    @Select("SELECT * FROM key")
    public List<ConnectKey> findAllConnectKey();

    @Select("SELECT * FROM key WHERE id = #{id}")
    public ConnectKey findConnectKeyById(String id);

    @Insert("INSERT INTO key(id, publicKey , aesKey , 'order' , timeStamp) VALUES (#{id}, #{publicKey} , #{aesKey} , #{order} , #{timeStamp})")
    public void insertConnectKey(ConnectKey connectKey);

    @Update("UPDATE key SET publicKey = #{publicKey} , aesKey = #{aesKey} , 'order' = #{order} , timeStamp = #{timeStamp} WHERE id = #{id}")
    public void updateConnectKey(ConnectKey connectKey);

}
