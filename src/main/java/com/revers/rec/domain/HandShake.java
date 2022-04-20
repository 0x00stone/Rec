package com.revers.rec.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ToString
public class HandShake {
    private String id;
    private Integer type;
    private String publicKey;
    private String aesKey;
    private Integer order;

}
