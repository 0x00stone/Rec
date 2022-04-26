package com.revers.rec.mapper;

import com.revers.rec.domain.Friend;
import com.revers.rec.domain.Group;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GroupMapper {
    @Insert("INSERT INTO 'group'(groupName) VALUES (#{groupName})")
    @SelectKey(statement = "SELECT HEX(RANDOMBLOB(16)) as groupId;", before = true, keyProperty = "groupId", keyColumn = "groupId",resultType = String.class)
    public void insertGroup(Group group);

    @Delete("DELETE FROM 'group' WHERE groupId = #{groupId}")
    public void deleteGroup(String groupId);

    @Select("SELECT * FROM 'group' WHERE groupId = #{groupId}")
    public Group findGroupById(String groupId);

    @Select("SELECT * FROM 'group' WHERE groupName = #{groupName}")
    public Group findGroupByName(String groupName);

    @Delete("DELETE FROM 'group' WHERE groupId = #{groupId}")
    public void deleteGroupById(String groupId);

    @Update("UPDATE friend SET groupName = #{groupName} WHERE groupId = #{groupId}")
    public void updateGroup(Group group);
}
