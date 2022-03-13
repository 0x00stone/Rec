package com.revers.rec.domain;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
public class user implements Serializable {


    private String id; //hash(publicKey)
    private String username;
    private String publicKey; //唯一身份
    private String privateKey; //唯一身份
    private String nodeId; // 节点id
    private String sign; // 签名
    private Integer onlineTime; // 上线时间 从 1970-01-01 00:00:00 UTC 算起的秒数。
    private String ipv6;
    private String ipv6Port;
    private String ipv4;
    private String ipv4Port;
    private boolean status; //状态, true 在线 , false 离线

    private String aeskey; // 数据库加密用的key

    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", sign='" + sign + '\'' +
                ", onlineTime=" + onlineTime +
                ", ipv6='" + ipv6 + '\'' +
                ", ipv6Port='" + ipv6Port + '\'' +
                ", ipv4='" + ipv4 + '\'' +
                ", ipv4Port='" + ipv4Port + '\'' +
                ", status=" + status +
                ", aeskey='" + aeskey + '\'' +
                '}';
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Integer onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getIpv6() {
        return ipv6;
    }

    public void setIpv6(String ipv6) {
        this.ipv6 = ipv6;
    }

    public String getIpv6Port() {
        return ipv6Port;
    }

    public void setIpv6Port(String ipv6Port) {
        this.ipv6Port = ipv6Port;
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    public String getIpv4Port() {
        return ipv4Port;
    }

    public void setIpv4Port(String ipv4Port) {
        this.ipv4Port = ipv4Port;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getAeskey() {
        return aeskey;
    }

    public void setAeskey(String aeskey) {
        this.aeskey = aeskey;
    }
}
