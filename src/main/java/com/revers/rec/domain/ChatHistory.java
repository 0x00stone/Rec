package com.revers.rec.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @description 聊天记录返回消息
 */
@Getter
@Setter
public class ChatHistory {
    private String id;
    private String username;
    private String userId;
    private String avatar;
    private String content;
    private Long timestamp;

    public ChatHistory(String id, String username, String userId, String avatar, String content, Long timestamp) {
        this.id = id;
        this.username = username;
        this.userId = userId;
        this.avatar = avatar;
        this.content = content;
        this.timestamp = timestamp;
    }
}