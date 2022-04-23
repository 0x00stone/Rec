package com.revers.rec.config;

public class optionConfig {
    //client
    private static Integer clientCommunicateRunTimeOut = 150000;
    private static Integer clientHandShakeClientRunTimeOut = 15000;
    private static Integer clientClientPingRunTimeOut = 15000;

    //server
    private static Integer ServerListenPort = 30000;

    public static Integer getServerListenPort() {
        return ServerListenPort;
    }

    public static void setServerListenPort(Integer serverListenPort) {
        ServerListenPort = serverListenPort;
    }

    public static Integer getClientClientPingRunTimeOut() {
        return clientClientPingRunTimeOut;
    }

    public static void setClientClientPingRunTimeOut(Integer clientClientPingRunTimeOut) {
        optionConfig.clientClientPingRunTimeOut = clientClientPingRunTimeOut;
    }

    public static Integer getClientCommunicateRunTimeOut() {
        return clientCommunicateRunTimeOut;
    }

    public static void setClientCommunicateRunTimeOut(Integer clientCommunicateRunTimeOut) {
        optionConfig.clientCommunicateRunTimeOut = clientCommunicateRunTimeOut;
    }

    public static Integer getClientHandShakeClientRunTimeOut() {
        return clientHandShakeClientRunTimeOut;
    }

    public static void setClientHandShakeClientRunTimeOut(Integer clientHandShakeClientRunTimeOut) {
        optionConfig.clientHandShakeClientRunTimeOut = clientHandShakeClientRunTimeOut;
    }
}
