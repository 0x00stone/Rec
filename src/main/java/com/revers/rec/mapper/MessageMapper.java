package com.revers.rec.mapper;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.Message;
import com.revers.rec.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Mapper
public interface MessageMapper {

    //TODO 设置主键为UUID
    @Insert("insert into '${tableName}'(type,createTime,updateTime,isSender,isRead,strTalker,strContent)" +
            "values(#{message.type},#{message.createTime},#{message.updateTime},#{message.isSender},#{message.isRead},#{message.strTalker},#{message.strContent});")
    @SelectKey(statement = "SELECT seq messageId FROM sqlite_sequence WHERE (name = 'message')", before = false, keyProperty = "messageId", resultType = Integer.class)
    public boolean createMessage(Message message,String tableName);

    @Update("update '${tableName}' set isRead = #{isRead} ,updateTime = #{updateTime} where messageId = #{messageId};")
    public void readMessage(int messageId,Long updateTime,String tableName);

    @Select("select * from '${tableName}' ORDER BY messageId desc limit 20 ")
    public List<Message> findRecent(String tableName);

    @Delete("delete from ${tableName} where messageId = #{messageId};")
    public void deleteUser(Integer messageId);

    @Select("select * from ${tableName};")
    public List<Message> findAll();

    @Select("select * from ${tableName} where strTalker = #{strTalker};")
    public List<Message> findByTalker(String strTalker);

    @Select("select * from ${tableName} where messageId = #{messageId};")
    public Message findById(Integer messageId);





}
