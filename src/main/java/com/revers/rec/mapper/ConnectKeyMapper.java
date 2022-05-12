package com.revers.rec.mapper;

import com.revers.rec.domain.ConnectKey;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConnectKeyMapper {

    @Delete("DELETE FROM key WHERE id1 = #{id1} and id2 = #{id2}")
    public void deleteConnectKeyById(String id1,String id2);

    @Select("SELECT * FROM key")
    public List<ConnectKey> findAllConnectKey();

    @Select("SELECT * FROM key WHERE id1 = #{id1} and id2 = #{id2}")
    public ConnectKey findConnectKeyById(String id1,String id2);

    @Insert("INSERT INTO key(id,id1,id2, publicKey , aesKey , 'order' , timeStamp) VALUES (#{id},#{id1},#{id2}, #{publicKey} , #{aesKey} , #{order} , #{timeStamp})")
    @SelectKey(statement = "SELECT HEX(RANDOMBLOB(16)) as id;", before = true, keyProperty = "id", keyColumn = "id",resultType = String.class)
    public void insertConnectKey(ConnectKey connectKey);

    @Update("UPDATE key SET publicKey = #{publicKey} , aesKey = #{aesKey} , 'order' = #{order} , timeStamp = #{timeStamp} WHERE id1 = #{id1} and id2 = #{id2}")
    public void updateConnectKey(ConnectKey connectKey);

}
