package com.revers.rec.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
public class ConnectKey {
    private String Id;
    private String publicKey;
    private String aesKey;
    private Integer order;
    private Long timeStamp;

}
