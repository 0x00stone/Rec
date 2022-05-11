package com.revers.rec.Kademlia.Bucket;

import com.revers.rec.Kademlia.Node.KademliaId;
import com.revers.rec.Kademlia.Node.Node;
import com.revers.rec.domain.vo.NodeVo;

import java.util.List;
import java.util.concurrent.ExecutionException;


public interface RoutingTable
{
    /**向路由表中添加节点n*/
    public  void insert(Node n) throws ExecutionException, InterruptedException;

    /**找最接近目标id的几个节点 */
    public  List<Node> findClosest(KademliaId target);

    /**列出表中所有节点  */
    public  List<Node> getAllNodes();

    /**
     * @return Bucket[] The buckets in this Kad Instance
     */
    public  Bucket[] getBuckets();

    /** 前端获取路由表 **/
    public List<NodeVo> getAllNodeVo();

}
