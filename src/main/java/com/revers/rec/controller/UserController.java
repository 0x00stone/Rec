package com.revers.rec.controller;

import com.revers.rec.service.user.UserServiceImpl;
import com.revers.rec.util.ConstantUtil;
import com.revers.rec.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userService;


    @RequestMapping("/register")
    @ResponseBody
    public ModelAndView register(String username, String password) throws NoSuchAlgorithmException {
        log.info("创建用户: " + username );

        HashMap data = new HashMap();
        if(isEmpty(username,password)){
            data.put("msg",username+ " 用户名或密码为空");
            return new ModelAndView("error",data);
        }

        Result flag = userService.register(username, password);
        data.put("msg",flag.getMsg());
        if (flag.getFlag() == ConstantUtil.SUCCESS) {
            return new ModelAndView("success",data);
        } else {
            return new ModelAndView("error",data);
        }
    }

    @RequestMapping("/login")
    @ResponseBody
    public ModelAndView login(String username, String password){
        log.info("用户: " + username + "登录");

        Map data = new HashMap();
        if(isEmpty(username,password)){
            data.put("msg",username+ " 用户名或密码为空");
            return new ModelAndView("error",data);
        }

        Result flag = userService.login(username,password);
        data.put("msg",flag.getMsg());
        if (flag.getFlag() == ConstantUtil.SUCCESS) {
            return new ModelAndView("success",data);
        } else {
            return new ModelAndView("error",data);
        }
    }

    @RequestMapping("/registerPage")
    public String register(){
        return "register";
    }

    private boolean isEmpty(String username,String password){
        return "".equals(username) || "".equals(password);
    }



}
