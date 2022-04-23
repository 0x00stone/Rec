package com.revers.rec.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.signature.qual.Identifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
@Setter
@ToString
public class Message implements Serializable {


    private String messageId;
    private Integer type;
    private Integer isSender;
    private Integer isRead;
    private String strTalker;
    private String strContent;
    private Long createTime;
    private Long updateTime;
}
