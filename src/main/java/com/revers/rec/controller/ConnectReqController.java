package com.revers.rec.controller;

import com.revers.rec.util.Result;
import org.springframework.jdbc.core.metadata.HanaCallMetaDataProvider;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/*/*
 *
 * @author 74726
 * @date 2022/03/24 14:38
 * @param null
 * @return
 */
@RestController
@RequestMapping("/connect")
public class ConnectReqController {

    //  握手
    @RequestMapping("/login")
    public Result login(@RequestBody HashMap<String,Object> map){
        //同步自动传递aes;
        //分发用户表
        return null;
    }

    // 分手
    @RequestMapping("/logout")
    public Result logout(@RequestBody HashMap<String,Object> map){

        return null;
    }

}
