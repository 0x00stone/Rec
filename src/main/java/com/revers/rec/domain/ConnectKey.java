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
public class ConnectKey implements Serializable {
    private String id;
    private String id1;
    private String id2;
    private String publicKey;
    private String aesKey;
    private Integer order;
    private Long timeStamp;

}
