/*
package com.revers.rec.Cademlia;

import com.revers.rec.Kademlia.Node.KademliaId;
import com.revers.rec.Kademlia.Node.KeyComparator;
import com.revers.rec.Kademlia.Node.Node;
import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;
import org.checkerframework.checker.units.qual.K;
import org.junit.jupiter.api.Test;

public class testKey {

    @Test
    public void testKademliaId(){
        KademliaId id0 = new KademliaId("0000000000000000000000000000000000000000");
        KademliaId id1 = new KademliaId(new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,127});
        KademliaId id2 = new KademliaId(new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2});
        KademliaId id3 = new KademliaId(new byte[]{127,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3});
        KademliaId id4 = new KademliaId("ffffffffffffffffffffffffffffffffffffffff");
        System.out.println(id0.getDistance(id1));
        System.out.println(id0.getDistance(id2));
        System.out.println(id0.getDistance(id3));
        KeyComparator comparator = new KeyComparator(id1);
        System.out.println(comparator.compare(new Node(id0,"",0,""),new Node(id4,"",0,"")));
        System.out.println(comparator.compare(new Node(id4,"",0,""),new Node(id3,"",0,"")));
        System.out.println(comparator.compare(new Node(id0,"",0,""),new Node(id2,"",0,"")));
        System.out.println(id1.getInt());
        System.out.println(id2.getInt());
        System.out.println(id3.getInt());
    }




}
*/
