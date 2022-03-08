package com.revers.rec.controller;

import com.revers.rec.service.userServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class userController {

    @Autowired
    private userServiceImpl userService;

    @ResponseBody
    @RequestMapping("/query")
    public String queryUserList(){
        return userService.queryUserList().get(0).toString();
    }
}
