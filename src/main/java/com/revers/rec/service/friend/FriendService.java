package com.revers.rec.service.friend;

import com.revers.rec.domain.Friend;

import java.util.List;

public interface FriendService {

    public void addFriend(String friendPublicKey,String friendName);

    public void deleteFriendByName(String friendName);


    Friend findFriendByName(String friendName);

    List<Friend> findAllFriend();

    void updateFriend(String id, String friendPublicKey, String friendName);

    Friend findFriendByFriendPublicKey(String friendPublicKey);
}
