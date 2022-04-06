package com.revers.rec.mapper;

import com.revers.rec.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Delete("delete from user where username = #{username};")
    public void deleteUser(String username);

    @Select("select * from user;")
    public List<User> findAll();

    @Select("select * from user where username = #{username};")
    public User findUserByUsername(String username);


    @Insert("insert into user(id,username,publicKey,privateKey,aeskey)values(#{id},#{username},#{publicKey},#{privateKey},#{aeskey});")
    public boolean createUser(User user);


}
