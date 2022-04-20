package com.revers.rec.Kademlia.Bucket;

import com.revers.rec.Kademlia.Node.Node;
import com.revers.rec.net.Client.ClientOperation;
import com.revers.rec.util.Result;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ExecutionException;

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
    public synchronized void insert(Node n) throws ExecutionException, InterruptedException {
        if(containsNode(n)){
            removeNode(n);
            bucket.add(n);
            depth++;
            log.info("bucket " + this.getBucketId() + " 移除并插入第 " +  this.getDepth() + " 个节点" + n.getNodeId());
        }else {
            if (depth < RoutingTableImpl.k) {
                bucket.add(n);
                depth++;
                log.info("bucket " + this.getBucketId() + " 插入第 " +  this.getDepth() + " 个节点" + n.getNodeId());

            }else {
                Result ping = ClientOperation.ping(bucket.getFirst().getInetAddress(), bucket.getFirst().getPort());
                if(ping.getFlag()){
                    Node first = bucket.getFirst();
                    bucket.removeFirst();
                    bucket.add(first);
                    //成功
                }else{
                    bucket.removeFirst();
                    bucket.add(n);
                    //失败
                }
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
            log.info("bucket " + this.getBucketId() + " 移除第 " +  this.getDepth() + " 个节点成功" + n.getNodeId());
            depth--;
            return true;
        }
        log.info("bucket " + this.getBucketId() + " 移除第 " +  this.getDepth() + " 个节点失败" + n.getNodeId());
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
