package com.revers.rec.service.group;

import com.revers.rec.domain.Group;

public interface GroupService {
    void insertGroup(String groupName);

    Group findGroupById(String groupId);

    Group findGroupByName(String groupName);

    void deleteGroupById(String groupId);

    void updateGroup(Group group);
}
