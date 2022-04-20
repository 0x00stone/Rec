package com.revers.rec.mapper;

import com.revers.rec.RecApplication;
import com.revers.rec.domain.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecApplication.class)
public class testMessageMapper {

    /*
     @Delete("delete from message where messageId = #{messageId};")
    public void deleteUser(Integer messageId);

    @Select("select * from message;")
    public List<Message> findAll();

    @Select("select * from message where strTalker = #{strTalker};")
    public List<Message> findByTalker(String strTalker);

    @Select("select * from message where messageId = #{messageId};")
    public Message findById(Integer messageId);

    @Select("select t.* from(select rownum,m.* from message m order by m.messageId desc) t where rownum<11")
    public List<Message> findRecent();

    @Insert("insert into message(messageId,type,createTime,isSender,isRead,strTalker,strContent)" +
            "values(#{messageId},#{type},#{createTime},#{isSender},#{isRead},#{strTalker},#{strContent});")
    @SelectKey(keyProperty = "messageId",resultType = String.class, before = true,
            statement = "select replace(uuid(), '-', '') as id from dual")
    public boolean createMessage(Message message);*/
    @Autowired
    MessageMapper messageMapper;
    @Test
    public void testMessageMapper() {
        /*Message[] messages = new Message[100];
        for(int i = 0 ; i < 100 ; i++){
            messages[i] = new Message();
            messages[i].setType(1);
            messages[i].setCreateTime(System.currentTimeMillis());
            messages[i].setIsSender(1);
            messages[i].setMessageId(1);
            messages[i].setStrTalker("testTalker");
            messages[i].setStrContent("testContent" + i);
            messageMapper.createMessage(messages[i]);
        }*/
        for(Message message:messageMapper.findByTalker("testTalker")){
            System.out.println(message);
        }
        System.out.println("-------------------");
        for(Message message:messageMapper.findRecent()){
            System.out.println(message);
        }
    }
}
