package com.revers.rec.controller;

import com.alibaba.fastjson.JSON;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.domain.Friend;
import com.revers.rec.domain.Group;
import com.revers.rec.domain.json.JsonGroup;
import com.revers.rec.domain.json.JsonUser;
import com.revers.rec.service.friend.FriendService;
import com.revers.rec.service.group.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
public class InitController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private GroupService groupService;



    @RequestMapping("/getList")
    @ResponseBody
    public String loadInitPage() {

        HashMap<String,JsonGroup> groupMap = new HashMap<>();// <id,jsGroup[]>

        Group stranger = groupService.findGroupByName("陌生人");
        if(stranger == null){
            groupService.insertGroup("陌生人");
            stranger = groupService.findGroupByName("陌生人");
        }
        groupMap.put(stranger.getGroupId(),new JsonGroup(stranger.getGroupName(),stranger.getGroupId(),"online",new ArrayList<>()));

        for(Friend friend : friendService.findAllFriend()){
            JsonUser user = new JsonUser(friend.getFriendName(),friend.getFriendId(),friend.getStatus(),friend.getSign(),friend.getPortrait());
            if(friend.getGroupId() == null || "".equals(friend.getGroupId())){
                groupMap.get(stranger.getGroupId()).getList().add(user);
            }
            if(groupMap.containsKey(friend.getGroupId())){
                JsonGroup group = groupMap.get(friend.getGroupId());

                group.getList().add(user);
            }else{
                Group group = groupService.findGroupById(String.valueOf(friend.getGroupId()));
                groupMap.put(friend.getGroupId(),new JsonGroup(group.getGroupName(),group.getGroupId(),"online",new ArrayList<>()));
                groupMap.get(friend.getGroupId()).getList().add(user);
            }
        }

        ArrayList<JsonGroup> jsonGroups = new ArrayList<>();
        for(Map.Entry<String,JsonGroup> entry : groupMap.entrySet()){
            jsonGroups.add(entry.getValue());
        }

        JsonUser mine = new JsonUser();
        mine.setUsername(AccountConfig.getUsername());
        mine.setId(AccountConfig.getId());
        mine.setAvatar(AccountConfig.getPortrait());
        mine.setSign(AccountConfig.getSign());
        mine.setStatus(AccountConfig.getStatus());

        HashMap<String,Object> data = new HashMap<>();
        data.put("friend",jsonGroups);
        data.put("mine",mine);

        HashMap<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "success");
        map.put("data", data);

        return JSON.toJSONString(map);


    }
}
