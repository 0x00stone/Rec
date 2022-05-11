package com.revers.rec.domain.json;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Getter
@Setter
public class JsonUser{
    private String username;
    private String id;
    private String status;
    private String sign;
    private String avatar;
    private String publicKey;

    public JsonUser() {}

    public JsonUser(String username, String id, String status, String sign, String avatar) {
        this.username = username;
        this.id = id;
        this.status = status;
        this.sign = sign;
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", id=" + id + ", status=" + status + ", sign=" + sign + ", avatar=" + avatar + "]";
    }
}
