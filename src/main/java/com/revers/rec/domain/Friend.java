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
public class Friend implements Serializable {

    private String id;
    private String myId;
    private String friendPublicKey;
    private String friendName;
    private Long createTime;

}
