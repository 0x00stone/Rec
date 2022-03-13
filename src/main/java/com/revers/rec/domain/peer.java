package com.revers.rec.domain;

import java.io.Serializable;
import java.util.Date;

public class peer implements Serializable {
    private Long id; //hash(publicKey)
    private String username;
    private String publicKey; //唯一身份
    private String nodeId; // 节点id
    private String sign; // 签名
    private Date onlineTime; // 上线时间
    private String ipv6;
    private String port;
    private boolean status; //状态, true 在线 , false 离线
}
