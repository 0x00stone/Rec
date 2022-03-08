package com.revers.rec.mapper;

import com.revers.rec.domain.user;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface userMapper {

    @Select("select * from user")
    public List<user> queryUserList();
}
