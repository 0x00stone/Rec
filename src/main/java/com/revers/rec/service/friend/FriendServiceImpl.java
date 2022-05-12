package com.revers.rec.service.friend;

import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.Friend;
import com.revers.rec.mapper.FriendMapper;
import com.revers.rec.util.cypher.DigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    private FriendMapper friendMapper;

    @Override
    public void addFriend(String friendName,String friendPublicKey,String groupId) {
        Friend friend = new Friend();
        friend.setMyId(AccountConfig.getId());
        friend.setFriendPublicKey(friendPublicKey);
        friend.setFriendName(friendName);
        friend.setCreateTime(System.currentTimeMillis());
        friend.setGroupId(groupId);
        friend.setFriendId(DigestUtil.Sha1AndSha256(friendPublicKey));
        friend.setPortrait("http://127.0.0.1:9000/static/image/avatar/avatar"+ (int)Math.ceil(Math.random()*10)+".jpg");
        friendMapper.insertFriend(friend);
    }

    @Override
    public void deleteFriendById(String friendId) {
        friendMapper.deleteFriendByFriendId(AccountConfig.getId(),friendId);
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
        return friendMapper.findFriendByPublicKey(friendPublicKey,AccountConfig.getId());
    }

    @Override
    public Boolean changeGroup(String friendId, String groupId) {
        return friendMapper.changeGroup(friendId,groupId);
    }

    @Override
    public Friend findFriendByFriendId(String friendId){
        return friendMapper.findFriendByFriendId(AccountConfig.getId(),friendId);
    }
}
