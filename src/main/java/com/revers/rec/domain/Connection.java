package com.revers.rec.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Connection implements Serializable {
    private String srcId;
    private String destId;
    private String srcPublicKey;
    private String destPublicKey;
    private Data data;
}
