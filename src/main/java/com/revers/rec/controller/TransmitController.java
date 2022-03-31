package com.revers.rec.controller;

import com.revers.rec.util.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/")
public class TransmitController {

    @RequestMapping("/rec")
    public Result receive(@RequestBody HashMap<String,Object> rec){
        return null;
    }
}
