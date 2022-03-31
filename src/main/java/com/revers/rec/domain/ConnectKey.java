package com.revers.rec.domain;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ConnectKey {
    private String Id;
    private String publicKey;
    private String aesKey;

    @Override
    public String toString() {
        return "ConnectKey{" +
                "Id='" + Id + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", aesKey='" + aesKey + '\'' +
                '}';
    }
}
