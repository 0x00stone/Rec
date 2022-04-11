package com.revers.rec.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;


@Configuration
public class AccountConfig {
    private static String id; //hash(publicKey)
    private static String username;
    private static String publicKey; //唯一身份
    private static String privateKey; //唯一身份
    private static Integer onlineTime; // 上线时间 从 1970-01-01 00:00:00 UTC 算起的秒数。
    private static String ipv6; //RecApplication 添加
    @Value("${rec.config.listingPort}")
    private static Integer ipv6Port; // 配置 添加
    private static boolean status; //状态, true 在线 , false 离线

    private static String aeskey; // 数据库加密用的key

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        AccountConfig.id = id;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        AccountConfig.username = username;
    }

    public static String getPublicKey() {
        return publicKey;
    }

    public static void setPublicKey(String publicKey) {
        AccountConfig.publicKey = publicKey;
    }

    public static String getPrivateKey() {
        return privateKey;
    }

    public static void setPrivateKey(String privateKey) {
        AccountConfig.privateKey = privateKey;
    }

    public static Integer getOnlineTime() {
        return onlineTime;
    }

    public static void setOnlineTime(Integer onlineTime) {
        AccountConfig.onlineTime = onlineTime;
    }

    public static String getIpv6() {
        return ipv6;
    }

    public static void setIpv6(String ipv6) {
        AccountConfig.ipv6 = ipv6;
    }

    public static Integer getIpv6Port() {
        return ipv6Port;
    }

    public static void setIpv6Port(Integer ipv6Port) {
        AccountConfig.ipv6Port = ipv6Port;
    }

    public static boolean isStatus() {
        return status;
    }

    public static void setStatus(boolean status) {
        AccountConfig.status = status;
    }

    public static String getAeskey() {
        return aeskey;
    }

    public static void setAeskey(String aeskey) {
        AccountConfig.aeskey = aeskey;
    }

    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", onlineTime=" + onlineTime +
                ", ipv6='" + ipv6 + '\'' +
                ", ipv6Port='" + ipv6Port + '\'' +
                ", status=" + status +
                ", aeskey='" + aeskey + '\'' +
                '}';
    }
}
