package com.revers.rec.service.friend;

import com.revers.rec.domain.Friend;

import java.util.List;

public interface FriendService {

    void addFriend(String friendName, String friendPublicKey, String groupId);

    void deleteFriendById(String friendId);

    Friend findFriendByName(String friendName);

    List<Friend> findAllFriend();

    void updateFriend(String id, String friendPublicKey, String friendName);

    Friend findFriendByFriendPublicKey(String friendPublicKey);

    Boolean changeGroup(String friendId, String groupId);

    Friend findFriendByFriendId(String friendId);
}
