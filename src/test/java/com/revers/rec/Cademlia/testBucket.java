package com.revers.rec.Cademlia;

import com.revers.rec.Kademlia.Bucket.Bucket;
import com.revers.rec.Kademlia.Bucket.BucketImpl;
import com.revers.rec.Kademlia.Node.KademliaId;
import com.revers.rec.Kademlia.Node.Node;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class testBucket {
    ArrayList<Bucket> list = new ArrayList<>();

    @Test
    public void testBucket() throws ExecutionException, InterruptedException {
        for (int i = 1; i<= 160; i++) {
            list.add(new BucketImpl(i));
        }
        list.get(1);
        list.get(0);
        list.get(1).insert(new Node(new KademliaId("0000000000000000000000000000000000000001"),"127.0.0.1",123,"key"));
        list.get(1).insert(new Node(new KademliaId("0000000000000000000000000000000000000010"),"127.0.0.1",123,"key"));
        System.out.println(list.get(1).getDepth());
        System.out.println(list.get(1).getDepth());
        list.get(1).insert(new Node(new KademliaId("0000000000000000000000000000000000000001"),"127.0.0.1",123,"key"));
        System.out.println(list.get(1).getDepth());

    }
}
