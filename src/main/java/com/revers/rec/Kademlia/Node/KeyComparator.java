package com.revers.rec.Kademlia.Node;

import java.math.BigInteger;
import java.util.Comparator;

public class KeyComparator implements Comparator<Node>
{

    private final BigInteger key;

    public KeyComparator(KademliaId key)
    {
        this.key = key.getInt();
    }

    /** 比较n1 n2 哪个离目标节点最近
     * n1 比 n2 近或相等 返回-1
     * n1 比 n2 远 返回 1
     * **/
    @Override
    public int compare(Node n1, Node n2)
    {
        BigInteger b1 = n1.getNodeId().getInt();
        b1 = b1.xor(key);

        BigInteger b2 = n2.getNodeId().getInt();
        b2 = b2.xor(key);

        return b1.abs().compareTo(b2.abs());
    }
}

