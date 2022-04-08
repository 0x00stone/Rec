package com.revers.rec.Kademlia.Node;


import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;

public class KademliaId implements Serializable
{

    public final transient static int ID_LENGTH = 160;
    private byte[] keyBytes;


    public KademliaId(String data)
    {
        if (data.length() % 2 == 1)
        {
            throw new IllegalArgumentException(data+ "字符串位数位 "+ data.length() +"位, 字符串位数错误");
        }

        byte[] bytes = new byte[data.length()/2];
        for(int i = 0 ; i < data.length()/2 ; i++){
            bytes[i] = (byte)Integer.parseInt(data.substring(i*2,i*2+2),16);
        }
        setKeyBytes(bytes);
    }

    /**
     * Generate the NodeId from a given byte[]
     *
     * @param bytes
     */
    public KademliaId(byte[] bytes)
    {
        setKeyBytes(bytes);
    }

    public void setKeyBytes(byte[] bytes) {
        if (bytes.length != ID_LENGTH / 8)
        {
            throw new IllegalArgumentException("Specified Data need to be " + (ID_LENGTH / 8) + " characters long. Data Given: '" + new String(bytes) + "'");
        }
        this.keyBytes = bytes;
    }

    public byte[] getBytes()
    {
        return this.keyBytes;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof KademliaId)
        {
            KademliaId nid = (KademliaId) o;
            return this.hashCode() == nid.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 83 * hash + Arrays.hashCode(this.keyBytes);
        return hash;
    }

    /** 异或 计算两点间的距离 **/
    public KademliaId xor(KademliaId nid)
    {
        byte[] result = new byte[ID_LENGTH / 8];
        byte[] nidBytes = nid.getBytes();

        for (int i = 0; i < ID_LENGTH / 8; i++)
        {
            result[i] = (byte) (this.keyBytes[i] ^ nidBytes[i]);
        }

        KademliaId resNid = new KademliaId(result);

        return resNid;
    }

    /** 获得第一个 '1' 的位置**/
    public int getFirstSetBitIndex()
    {
        int prefixLength = 0;

        for (byte b : this.keyBytes)
        {
            if (b == 0)
            {
                prefixLength += 8;
            }
            else
            {
                /* If the byte is not 0, we need to count how many MSBs are 0 */
                int count = 0;
                for (int i = 7; i >= 0; i--)
                {
                    boolean a = (b & (1 << i)) == 0;
                    if (a)
                    {
                        count++;
                    }
                    else
                    {
                        break;   // Reset the count if we encounter a non-zero number
                    }
                }

                /* Add the count of MSB 0s to the prefix length */
                prefixLength += count;

                /* Break here since we've now covered the MSB 0s */
                break;
            }
        }
        return prefixLength;
    }

    /** 获得两者间第一次不同到结尾的距离 **/
    public int getDistance(KademliaId to)
    {
        return ID_LENGTH - xor(to).getFirstSetBitIndex();// 计算两者的异或,获得第一个不同的位置
    }

    @Override
    public String toString()
    {
        BigInteger bi = new BigInteger(1, this.keyBytes);
        return String.format("%0" + (this.keyBytes.length << 1) + "X", bi);
    }

    /**
     * 返回十进制值
     */
    public BigInteger getInt()
    {
        return new BigInteger(1, this.getBytes());
    }

}