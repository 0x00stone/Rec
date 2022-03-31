package com.revers.rec.Kademlia.Node;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Node implements Serializable
{

    private KademliaId nodeId;
    private String inetAddress;
    private int port;
    private String publicKey;

    private String aesKey;

    public Node(KademliaId nid, String ip, int port,String publicKey)
    {
        this.nodeId = nid;
        this.inetAddress = ip;
        this.port = port;
        this.publicKey = publicKey;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Node)
        {
            Node n = (Node) o;
            if (n == this)
            {
                return true;
            }
            return this.getNodeId().equals(n.getNodeId());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return this.getNodeId().hashCode();
    }

    @Override
    public String toString()
    {
        return this.getNodeId().toString();
    }


}
