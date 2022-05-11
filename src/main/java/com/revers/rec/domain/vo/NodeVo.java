package com.revers.rec.domain.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeVo {
    private String nodeId;
    private String nodeAddress;
    private Integer bucketId;

    public NodeVo(String nodeId, String nodeAddress, Integer bucketId) {
        this.nodeId = nodeId;
        this.nodeAddress = nodeAddress;
        this.bucketId = bucketId;
    }
}
