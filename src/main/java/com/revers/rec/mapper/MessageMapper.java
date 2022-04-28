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
    @Insert("insert into '${tableName}'(type,createTime,updateTime,isSender,isRead,strTalker,strTalkerId,strContent)" +
            "values(#{message.type},#{message.createTime},#{message.updateTime},#{message.isSender},#{message.isRead},#{message.strTalker},#{message.strTalkerId},#{message.strContent});")
    @SelectKey(statement = "SELECT seq messageId FROM sqlite_sequence WHERE (name = 'message')", before = false, keyProperty = "messageId", resultType = Integer.class)
    public Integer createMessage(Message message,String tableName);

    @Select("select last_insert_rowid();")
    public Integer getLastInsertId();

    @Update("update '${tableName}' set isRead = 1 ,updateTime = #{updateTime} where messageId = #{messageId};")
    public void readMessage(int messageId,Long updateTime,String tableName);

    @Select("select * from '${tableName}' ORDER BY messageId desc limit 20 ")
    public List<Message> findRecent(String tableName);

    @Delete("delete from '${tableName}' where messageId = #{messageId};")
    public void deleteMessageId(String messageId,String tableName);

    @Select("select * from '${tableName}';")
    public List<Message> findAll(String tableName);

    @Select("select * from '${tableName}' where strTalker = #{strTalker} ORDER BY messageId desc;")
    public List<Message> findByTalker(String strTalker,String tableName);

    @Select("select * from '${tableName}' where strTalkerId = #{strTalkerId} ORDER BY messageId desc;")
    public List<Message> findByTalkerId(String strTalkerId,String tableName);

    @Select("select * from '${tableName}' where messageId = #{messageId};")
    public Message findById(Integer messageId,String tableName);

    @Select("select * from '${tableName}' where isRead = 0 ORDER BY messageId desc;")
    public List<Message> findUnreadByTalker(String tableName);


    @Select("select * from '${tableName}' where strTalker = #{strTalker} ORDER BY messageId desc LIMIT ((#{pages}-1)*#{pageSize}),#{pageSize};")
    public List<Message> findByTalkerLimit(String strTalker,String tableName,int pages,int pageSize);


    @Select("SELECT COUNT(*) FROM '${tableName}' WHERE strTalker = #{strTalker};")
    public Integer countMessage(String tableName,String strTalker);





}
