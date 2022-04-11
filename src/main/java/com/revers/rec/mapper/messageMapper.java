package com.revers.rec.mapper;

import com.revers.rec.domain.Message;
import com.revers.rec.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface messageMapper {
    @Delete("delete from message where messageId = #{messageId};")
    public void deleteUser(Integer messageId);

    @Select("select * from message;")
    public List<Message> findAll();

    @Select("select * from message where strTalker = #{strTalker};")
    public List<Message> findByTalker(String strTalker);

    @Select("select * from message where messageId = #{messageId};")
    public Message findById(Integer messageId);

    @Select("select * from message limit 20")
    public List<Message> findRecent();

    @Insert("insert into message(type,createTime,isSender,isRead,strTalker,strContent)" +
            "values(#{type},#{createTime},#{isSender},#{isRead},#{strTalker},#{strContent});")
    public boolean createMessage(Message message);

}
