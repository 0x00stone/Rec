package com.revers.rec.mapper;

import com.revers.rec.domain.ConnectKey;
import com.revers.rec.domain.Friend;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FriendMapper {

    @Insert("INSERT INTO friend(myId , friendPublicKey , friendName ,createTime) VALUES (#{myId}, #{friendPublicKey} , #{friendName} , #{createTime})")
    @SelectKey(statement = "SELECT HEX(RANDOMBLOB(16)) as id;", before = true, keyProperty = "id", keyColumn = "id",resultType = String.class)
    public void insertFriend(Friend friend);

    @Delete("DELETE FROM friend WHERE id = #{id}")
    public void deleteFriend(String id);

    @Select("SELECT * FROM friend WHERE id = #{id}")
    public Friend findFriendById(String id);

    @Select("SELECT * FROM friend WHERE friendPublicKey = #{friendPublicKey}")
    public Friend findFriendByPublicKey(String friendPublicKey);

    @Select("SELECT * FROM friend WHERE friendName = #{friendName}")
    public Friend findFriendByName(String friendName);

    @Delete("DELETE FROM friend WHERE friendName = #{friendName}")
    public void deleteFriendByName(String friendName);

    @Select("SELECT * FROM friend WHERE myId = #{myId}")
    public List<Friend> findAllFriend(String myId);

    @Update("UPDATE friend SET friendPublicKey = #{friendPublicKey} , friendName = #{friendName} , 'createTime' = #{createTime}  WHERE id = #{id}")
    public void updateFriend(Friend friend);
}
