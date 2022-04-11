package com.revers.rec.Kademlia.Bucket;

import com.revers.rec.Kademlia.Node.KademliaId;
import com.revers.rec.Kademlia.Node.KeyComparator;
import com.revers.rec.Kademlia.Node.Node;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.controller.Server.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

public class RoutingTableImpl implements RoutingTable{
    public static final int k = 20; // 每个桶最大节点数
    public static final int p = 3; // 每次向其他node发送请求时,会向p个最近节点发送数据

    private Node localNode;
    private ArrayList<Bucket> list;

    public RoutingTableImpl(){
        this.localNode = new Node(new KademliaId(AccountConfig.getId()),"127.0.0.1", Server.PORT,AccountConfig.getPublicKey());
        list = new ArrayList<>();
        initialize();
    }

    public void initialize() {
        for (int i = 1; i<= 160; i++) {
            list.add(new BucketImpl(i));
        }
    }

    /** 根据距离插入一个节点**/
    public synchronized void insert(Node n) throws ExecutionException, InterruptedException {
        list.get(localNode.getNodeId().getDistance(n.getNodeId())).insert(n);
    }


    public synchronized List<Node> findClosest(KademliaId target) {
        TreeSet<Node> sortedSet = new TreeSet<>(new KeyComparator(target));
        sortedSet.addAll(this.getAllNodes());

        List<Node> closest = new ArrayList<>(p);

        /* 获得排序set,获取前面几个节点 */
        int count = 0;
        for (Node n : sortedSet)
        {
            closest.add(n);
            if (++count == p)
            {
                break;
            }
        }
        return closest;
    }

    @Override
    public synchronized List getAllNodes() {
        List<Node> nodes = new ArrayList<>();

        for (Bucket b : this.list)
        {
            for (Node n : b.getBucket())
            {
                nodes.add(n);
            }
        }
        return nodes;
    }

    @Override
    public synchronized Bucket[] getBuckets() {
        return this.list.toArray(new Bucket[0]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\nPrinting Routing Table Started ***************** \n");
        int totalContacts = 0;
        for (Bucket b : this.list)
        {
            if (b.getDepth() > 0)
            {
                sb.append("# nodes in Bucket with depth ");
                sb.append(b.getDepth());
                sb.append(": ");
                sb.append(b.toString());
                sb.append("\n");
            }
        }

        sb.append("\nTotal Contacts: ");
        sb.append(totalContacts);
        sb.append("\n\n");

        sb.append("Printing Routing Table Ended ******************** ");

        return sb.toString();
    }
}
