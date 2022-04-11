package com.revers.rec.mapper;

import com.revers.rec.domain.HandShake;
import com.revers.rec.domain.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HandShakeMapper {
    @Delete("delete from handShake where id = #{id};")
    public void deleteUser(String id);

    @Select("select * from handShake;")
    public List<HandShake> findAll();

    @Select("select * from handShake where id = #{id};")
    public HandShake findById(String id);


    @Insert("insert into handShake(id,type,publicKey,aesKey)values(#{id},#{type},#{publicKey},#{aeskey});")
    public boolean createUser(HandShake handShake);
}
