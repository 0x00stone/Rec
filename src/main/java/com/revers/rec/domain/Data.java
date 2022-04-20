package com.revers.rec.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;
@Component
@Getter
@Setter
@ToString
public class Data {
    private String srcPublicKey;
    private String destPublicKey;
    private String data;
    private String signature;
    private String timeStamp;
}
