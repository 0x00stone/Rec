package com.revers.rec.service.friend;

import com.revers.rec.domain.Friend;

import java.util.List;

public interface FriendService {

    public void addFriend(String friendPublicKey,String friendName);

    void addFriend(String friendName, String friendPublicKey, String groupId);

    void deleteFriendById(String friendId);

    Friend findFriendByName(String friendName);

    List<Friend> findAllFriend();

    void updateFriend(String id, String friendPublicKey, String friendName);

    Friend findFriendByFriendPublicKey(String friendPublicKey);

    Boolean changeGroup(String friendId, String groupId);
}
