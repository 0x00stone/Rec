package com.revers.rec.service.friend;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.Friend;
import com.revers.rec.mapper.FriendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    private FriendMapper friendMapper;

    @Override
    public void addFriend(String friendName,String friendPublicKey) {
        Friend friend = new Friend();
        friend.setMyId(AccountConfig.getId());
        friend.setFriendPublicKey(friendPublicKey);
        friend.setFriendName(friendName);
        friend.setCreateTime(System.currentTimeMillis());
        friendMapper.insertFriend(friend);
    }

    @Override
    public void deleteFriendByName(String friendName) {
        friendMapper.deleteFriendByName(friendName);
    }

    @Override
    public Friend findFriendByName(String friendName) {
        return friendMapper.findFriendByName(friendName);
    }

    @Override
    public List<Friend> findAllFriend() {
        return friendMapper.findAllFriend(AccountConfig.getId());
    }

    @Override
    public void updateFriend(String id, String friendPublicKey, String friendName) {
        Friend friend = friendMapper.findFriendById(id);
        friend.setFriendName(friendName);
        friend.setFriendPublicKey(friendPublicKey);
        friendMapper.updateFriend(friend);
    }

    @Override
    public Friend findFriendByFriendPublicKey(String friendPublicKey){
        return friendMapper.findFriendByPublicKey(friendPublicKey);
    }

}
