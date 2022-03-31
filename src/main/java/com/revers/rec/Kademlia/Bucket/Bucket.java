package com.revers.rec.Kademlia.Bucket;

import com.revers.rec.Kademlia.Node.Node;

import java.util.List;

public interface Bucket {

    /*** 向桶中插入一个节点*/
    public void insert(Node n);

    /**检查桶中是否包含 n 节点 */
    public boolean containsNode(Node n);

    /**从桶中移除n 节点  */
    public boolean removeNode(Node n);

    public List<Node> getBucket();

    public int getDepth();
}
