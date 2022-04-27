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
    
    //前端相关
    public static final Integer SUCCESS = 0;
    public static final Integer ERROR = 1;
    public static final String LOGGIN_SUCCESS = "登陆成功";
    public static final String LOGGIN_FAIL = "登陆失败";
    public static final String NONACTIVED = "用户未激活";
    public static final String REGISTER_SUCCESS = "注册成功";
    public static final String REGISTER_FAIL = "登陆失败";
    public static final String SUCCESS_MESSAGE = "操作成功";
    public static final String ERROR_MESSAGE = "操作失败";
    public static final String UPLOAD_SUCCESS = "上传成功";
    public static final String UPLOAD_FAIL = "上传失败";
    public static final String IMAGE_PATH = "/upload/image/";
    public static final String FILE_PATH = "/upload/file/";
    public static final String AVATAR_PATH = "/static/image/avatar/";
    public static final String GROUP_AVATAR_PATH = "/static/image/group/";
    public static final String DEFAULT_GROUP_NAME = "我的好友";







}
