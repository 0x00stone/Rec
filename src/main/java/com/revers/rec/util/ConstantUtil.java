package com.revers.rec.util;

public class ConstantUtil {
    //Ping
    public static final String PING_SUCCESS = "000";
    public static final String PING_FAILURE = "001";
    public static final Integer MSGTYPE_PING_REQUEST = 0;
    public static final Integer MSGTYPE_PING_RESPONSE = 1;

    //Handshake
    public static final Integer MSGTYPE_HANDSHAKE_1 = 2;
    public static final Integer MSGTYPE_HANDSHAKE_2 = 3;
    public static final Integer MSGTYPE_HANDSHAKE_3 = 4;
    public static final Integer MSGTYPE_HANDSHAKE_4 = 5;

    //Connection层
    public static final String CONNECTION_GETTING_PING = "000";
    public static final String CONNECTION_GETTING_NONE = "001";


    public static final String CONNECTION_CANT_RECEIVE = "002";
    public static final String CONNECTION_SIGNATURE_ERROR = "003";

    public static final String CONNECTION_TYPE1_SUCCESS = "010";
    public static final String CONNECTION_TYPE1_FAILURE = "011";


    public static final String TYPE_ILLEGAL = "401";




}
