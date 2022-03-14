package com.revers.rec.mapper;

import com.revers.rec.domain.user;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface userMapper {
    @Delete("delete from user where username = #{username};")
    public void deleteUser(String username);

    @Select("select * from user;")
    public List<user> selectAllUser();

    @Select("select * from user where username = #{username};")
    public user selectUser(String username);


    @Insert("insert into user(id,username,publicKey,privateKey,sign,aeskey)values(#{id},#{username},#{publicKey},#{privateKey},#{sign},#{aeskey});")
    public boolean createUser(user user);


}
