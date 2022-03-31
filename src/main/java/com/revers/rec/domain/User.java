package com.revers.rec.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
@Setter
@ToString
public class User implements Serializable {


    private String id; //hash(publicKey)
    private String username;
    private String publicKey; //唯一身份
    private String nodeId; // 节点id
    private Integer onlineTime; // 上线时间 从 1970-01-01 00:00:00 UTC 算起的秒数。
    private boolean status; //状态, true 在线 , false 离线

    private String privateKey; //唯一身份
    private String aeskey; // 数据库加密用的key

}
