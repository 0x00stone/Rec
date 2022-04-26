package com.revers.rec.service.group;

import com.revers.rec.domain.Group;
import com.revers.rec.mapper.GroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Override
    public void insertGroup(String groupName){
        Group group = new Group();
        group.setGroupName(groupName);
        groupMapper.insertGroup(group);
    }

    @Override
    public Group findGroupById(String groupId){
        return groupMapper.findGroupById(groupId);
    }

    @Override
    public Group findGroupByName(String groupName){
        return groupMapper.findGroupByName(groupName);
    }


    @Override
    public void deleteGroupById(String groupId){
        groupMapper.deleteGroupById(groupId);
    }

    @Override
    public void updateGroup(Group group){
        groupMapper.updateGroup(group);
    }

}
