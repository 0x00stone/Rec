package com.revers.rec.mapper;

import com.revers.rec.domain.ConnectKey;
import com.revers.rec.domain.Friend;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FriendMapper {

    @Insert("INSERT INTO friend(myId , friendPublicKey , friendName ,createTime,groupId,friendId,portrait) VALUES (#{myId}, #{friendPublicKey} , #{friendName} , #{createTime} , #{groupId},#{friendId},#{portrait})")
    @SelectKey(statement = "SELECT HEX(RANDOMBLOB(16)) as id;", before = true, keyProperty = "id", keyColumn = "id",resultType = String.class)
    public void insertFriend(Friend friend);

    @Delete("DELETE FROM friend WHERE myId = #{myId} and friendId = #{friendId}")
    public void deleteFriendByFriendId(String myId,String FriendId);

    @Select("SELECT * FROM friend WHERE id = #{id}")
    public Friend findFriendById(String id);

    @Select("SELECT * FROM friend WHERE friendPublicKey = #{friendPublicKey} and myId = #{myId}")
    public Friend findFriendByPublicKey(String friendPublicKey,String myId);

    @Select("SELECT * FROM friend WHERE myId = #{myId} and friendId = #{friendId}")
    public Friend findFriendByFriendId(String myId,String friendId);


    @Select("SELECT * FROM friend WHERE friendName = #{friendName}")
    public Friend findFriendByName(String friendName);

    @Select("SELECT * FROM friend WHERE myId = #{myId}")
    public List<Friend> findAllFriend(String myId);

    @Update("UPDATE friend SET friendPublicKey = #{friendPublicKey} , friendName = #{friendName} , 'createTime' = #{createTime}  WHERE id = #{id}")
    public void updateFriend(Friend friend);

    @Update("UPDATE friend set groupId = #{groupId} where id = #{id}")
    public void updateGroupId(Friend friend);

    @Update("UPDATE friend set groupId = #{groupId} where friendId = #{friendId}")
    public boolean changeGroup(String friendId,String groupId);
}
