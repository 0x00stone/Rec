package com.revers.rec.mapper;

import com.revers.rec.RecApplication;
import com.revers.rec.service.friend.FriendService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecApplication.class)
public class testFriendMapper {
    @Autowired
    FriendService friendService;

}
