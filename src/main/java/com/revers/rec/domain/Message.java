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
public class Message implements Serializable {

    private Integer messageId;
    private Integer type;
    private Long createTime;
    private Integer isSender;
    private Integer isRead;
    private String strTalker;
    private String strContent;
}
