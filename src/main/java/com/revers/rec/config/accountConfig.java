package com.revers.rec.config;

import org.springframework.stereotype.Repository;

@Repository
public class accountConfig {
    private static String id; //hash(publicKey)
    private static String username;
    private static String publicKey; //唯一身份
    private static String privateKey; //唯一身份
    private static String nodeId; // 节点id
    private static String sign; // 签名
    private static Integer onlineTime; // 上线时间 从 1970-01-01 00:00:00 UTC 算起的秒数。
    private static String ipv6;
    private static String ipv6Port;
    private static String ipv4;
    private static String ipv4Port;
    private static boolean status; //状态, true 在线 , false 离线

    private static String aeskey; // 数据库加密用的key

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
    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        accountConfig.id = id;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        accountConfig.username = username;
    }

    public static String getPublicKey() {
        return publicKey;
    }

    public static void setPublicKey(String publicKey) {
        accountConfig.publicKey = publicKey;
    }

    public static String getPrivateKey() {
        return privateKey;
    }

    public static void setPrivateKey(String privateKey) {
        accountConfig.privateKey = privateKey;
    }

    public static String getNodeId() {
        return nodeId;
    }

    public static void setNodeId(String nodeId) {
        accountConfig.nodeId = nodeId;
    }

    public static String getSign() {
        return sign;
    }

    public static void setSign(String sign) {
        accountConfig.sign = sign;
    }

    public static Integer getOnlineTime() {
        return onlineTime;
    }

    public static void setOnlineTime(Integer onlineTime) {
        accountConfig.onlineTime = onlineTime;
    }

    public static String getIpv6() {
        return ipv6;
    }

    public static void setIpv6(String ipv6) {
        accountConfig.ipv6 = ipv6;
    }

    public static String getIpv6Port() {
        return ipv6Port;
    }

    public static void setIpv6Port(String ipv6Port) {
        accountConfig.ipv6Port = ipv6Port;
    }

    public static String getIpv4() {
        return ipv4;
    }

    public static void setIpv4(String ipv4) {
        accountConfig.ipv4 = ipv4;
    }

    public static String getIpv4Port() {
        return ipv4Port;
    }

    public static void setIpv4Port(String ipv4Port) {
        accountConfig.ipv4Port = ipv4Port;
    }

    public static boolean isStatus() {
        return status;
    }

    public static void setStatus(boolean status) {
        accountConfig.status = status;
    }

    public static String getAeskey() {
        return aeskey;
    }

    public static void setAeskey(String aeskey) {
        accountConfig.aeskey = aeskey;
    }
}
