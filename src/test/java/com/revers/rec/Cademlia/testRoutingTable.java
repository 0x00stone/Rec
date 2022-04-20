/*
package com.revers.rec.Cademlia;

import com.revers.rec.Kademlia.Bucket.Bucket;
import com.revers.rec.Kademlia.Bucket.RoutingTable;
import com.revers.rec.Kademlia.Bucket.RoutingTableImpl;
import com.revers.rec.Kademlia.Node.KademliaId;
import com.revers.rec.Kademlia.Node.Node;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class testRoutingTable {
    RoutingTable routingTable;
    @Test
    public void testRoutingTable() throws ExecutionException, InterruptedException {
        Node localNode = new Node(new KademliaId("0000000000000000000000000000000000000006"),"127.0.0.1",111,"key1");
        routingTable = new RoutingTableImpl();
        routingTable.insert(new Node(new KademliaId("0000000000000000000000000000000000000004"),"127.0.0.1",111,"4"));
        routingTable.insert(new Node(new KademliaId("0000000000000000000000000000000000000007"),"127.0.0.1",111,"7"));
        routingTable.insert(new Node(new KademliaId("0000000000000000000000000000000000000005"),"127.0.0.1",111,"5"));


        routingTable.insert(new Node(new KademliaId("0000000000000000000000000000000000000010"),"127.0.0.1",111,"key2"));
        routingTable.insert(new Node(new KademliaId("0000000000000000000000000000000000000100"),"127.0.0.1",111,"key4"));
        routingTable.insert(new Node(new KademliaId("0000000000000000000000000000000000001000"),"127.0.0.1",111,"key8"));
        routingTable.insert(new Node(new KademliaId("0000000000000000000000000000000000010000"),"127.0.0.1",111,"key16"));
        routingTable.insert(new Node(new KademliaId("0000000000000000000000000000000000100000"),"127.0.0.1",111,"key32"));
        routingTable.insert(new Node(new KademliaId("0000000000000000000000000000000001000000"),"127.0.0.1",111,"key64"));
        routingTable.insert(new Node(new KademliaId("0000000000000000000000000000000010000000"),"127.0.0.1",111,"key128"));
        routingTable.insert(new Node(new KademliaId("0000000000000000000000000000000000000010"),"127.0.0.1",111,"key3"));
        for(Node n :routingTable.findClosest(new KademliaId("0000000000000000000000000000000000000006"))){
            System.out.println(n.getPublicKey());
        }
        System.out.println();
        for(Node n:routingTable.getAllNodes()){
            System.out.println(n.getPublicKey());
        }
        System.out.println(routingTable.toString());

        System.out.println("------------getBuckets------");
        for(Bucket temp :routingTable.getBuckets()){
            System.out.println(temp);
        }

    }
}
*/
