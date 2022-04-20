package com.revers.rec.controller;

import com.revers.rec.util.ResultUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/")
public class TransmitController {

    @RequestMapping("/rec")
    public ResultUtil receive(@RequestBody HashMap<String,Object> rec){
        return null;
    }
}
