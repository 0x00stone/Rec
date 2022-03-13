package com.revers.rec.mapper;

import com.revers.rec.domain.user;
import org.apache.ibatis.annotations.*;

@Mapper
public interface userMapper {

    @Select("select * from user where username = #{username};")
    public user selectUser(String username);

    @ResultMap(value = {"user"})
    @Insert("insert into user(id,username,publicKey,privateKey,sign,aeskey)values(#{id},#{username},#{publicKey},#{privateKey},#{sign},#{aeskey});")
    public void createUser(user user);


}
