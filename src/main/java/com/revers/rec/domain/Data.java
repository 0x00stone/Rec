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
public class Data implements Serializable {
    private String srcId;
    private String destId;
    private String srcPublicKey;
    private String destPublicKey;
    private String orderId;
    private int msgType;
    private String data;
    private String signature;
    private String timeStamp;
}
