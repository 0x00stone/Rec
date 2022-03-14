package com.revers.rec.controller;

import com.revers.rec.config.accountConfig;
import com.revers.rec.service.userServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


@Controller
public class userController {
    @Autowired
    private accountConfig accountConfig;

    @Autowired
    private userServiceImpl userService;


    @RequestMapping("/register")
    public ModelAndView register(String username,String password,Model model) throws NoSuchAlgorithmException {
        System.out.println("create user");

        Map data = new HashMap();
        if(isEmpty(username,password)){
            data.put("msg",username+ " 用户名或密码为空");
            return new ModelAndView("error",data);
        }

        int flag = userService.register(username, password);
        if (flag == 0) {
            data.put("msg","注册成功");
            return new ModelAndView("success",data);
        } else if(flag == 1){
            data.put("msg",username+ " 当前已存在");
            return new ModelAndView("error",data);
        }else {
            data.put("msg",username+ " 注册失败,详情请见日志");
            return new ModelAndView("error",data);
        }
    }

    @RequestMapping("/login")
    public ModelAndView login(String username, String password, Model model) throws NoSuchAlgorithmException {
        System.out.println("select user");

        Map data = new HashMap();
        if(isEmpty(username,password)){
            data.put("msg",username+ " 用户名或密码为空");
            return new ModelAndView("error",data);
        }

        int flag = userService.login(username,password);
        if (flag == 0) {
            data.put("msg","登录成功");
            return new ModelAndView("success",data);
        } else if(flag == 1){
            data.put("msg",username+ " 当前不存在");
            return new ModelAndView("error",data);
        }else {
            data.put("msg",username+ " 登录失败,详情请见日志");
            return new ModelAndView("error",data);
        }
    }

    @RequestMapping("/registerPage")
    public String register(){
        return "register";
    }

    private boolean isEmpty(String username,String password){
        if("".equals(username) || "".equals(password))
            return true;
        return false;
    }








}
