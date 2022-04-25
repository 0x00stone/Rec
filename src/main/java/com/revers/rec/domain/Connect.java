package com.revers.rec.domain;

import com.revers.rec.util.cypher.Sha256Util;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Component
public class Connect implements Serializable {
    String connectSrcPublicKey = "";
    String connectDestPublicKey = "";
    String connectIpv6Port = "";
    String connectIpv6Ip = "";
    Long connectOrder = 0L;
    Integer connectMsgType = 0;
    Long connectTimestamp = 0L;
    String connectData = "";
    String connectSignature = "";

    public String getSignature(){
        String connectSrcPublicKeyHash = Sha256Util.getSHA256(this.connectSrcPublicKey);
        String connectDestPublicKeyHash = Sha256Util.getSHA256(this.connectDestPublicKey);
        String connectIpv6PortHash = Sha256Util.getSHA256(this.connectIpv6Port);
        String connectIpv6IpHash = Sha256Util.getSHA256(this.connectIpv6Ip);
        String connectOrderHash = Sha256Util.getSHA256(String.valueOf(this.connectOrder));
        String connectMsgTypeHash = Sha256Util.getSHA256(String.valueOf(this.connectMsgType));
        String connectTimestampHash = Sha256Util.getSHA256(String.valueOf(this.connectTimestamp));
        String connectDataHash = Sha256Util.getSHA256(this.connectData);
        return Sha256Util.getSHA256(
                connectSrcPublicKeyHash +
                connectDestPublicKeyHash +
                connectIpv6PortHash +
                connectIpv6IpHash +
                connectOrderHash +
                connectMsgTypeHash +
                connectTimestampHash +
                connectDataHash);
    }
}
/*
    Connect connect = new Connect();
    connect.setConnectSrcPublicKey();
    connect.setConnectDestPublicKey();
    connect.setConnectIpv6Port();
    connect.setConnectIpv6Ip();
    connect.setConnectOrder(new Random().nextLong());
    connect.setConnectMsgType();
    connect.setConnectTimestamp();
    connect.setConnectData();
    connect.setConnectSignature();
                */
