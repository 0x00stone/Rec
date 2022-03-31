package com.revers.rec.Kademlia.Bucket;

import com.revers.rec.Kademlia.Node.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Getter
@Setter
public class BucketImpl implements Bucket{

    private final int bucketId;
    private int depth;

    private LinkedList<Node> bucket;

    public BucketImpl(int bucketId){
        this.bucketId = bucketId;
        depth = 0;
        bucket = new LinkedList<>();
    }

    @Override
    public synchronized void insert(Node n) {
        if(containsNode(n)){
            removeNode(n);
            bucket.add(n);
            depth++;
            log.info("bucket" + this.getBucketId() + "移除并插入第" +  this.getDepth() + "个节点");
        }else {
            if (depth < RoutingTableImpl.k) {
                bucket.add(n);
                depth++;
                log.info("bucket" + this.getBucketId() + "插入第" +  this.getDepth() + "个节点");

            }else {
                // TODO ping 第一个节点,ping通第一个节点放到最后,否则抛弃第一个节点,将n放入链
            }
        }
    }

    @Override
    public synchronized boolean containsNode(Node n) {
        return bucket.contains(n);
    }

    @Override
    public synchronized boolean removeNode(Node n) {
        if(bucket.contains(n)){
            bucket.remove(n);
            log.info("bucket" + this.getBucketId() + "移除第" +  this.getDepth() + "个节点成功");
            depth--;
            return true;
        }
        log.info("bucket" + this.getBucketId() + "移除第" +  this.getDepth() + "个节点失败");
        return false;
    }

    @Override
    public String toString() {
        return "BucketImpl{" +
                "bucketId=" + bucketId +
                ", depth=" + depth +
                ", bucket=" + bucket +
                '}';
    }
}
