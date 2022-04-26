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
    public static final Integer FAILURE_SIGNATURE_MISMATCH = 7;

    //ClientCommunicate
    public static final String COMMUNICATE_SUCCESS = "060";
    public static final String COMMUNICATE_FAILURE = "061";
    public static final Integer MSGTYPE_COMMUNICATE = 6;

    //TimeStamp_FAILURE
    public static final Integer FAILURE_TIMESTAMP = 8;







}
