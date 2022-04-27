package com.revers.rec.websocket;

import com.alibaba.fastjson.JSON;
import com.revers.rec.domain.front.FrontMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
@Slf4j
public class DefaultHandler implements WebSocketHandler {

    /**
     * 建立连接
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 缓存用户信息: userInfo
    }

    /**
     * 接收消息
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message){
        System.out.println("接收到消息：" + message.getPayload());
        FrontMessage frontMessage = JSON.parseObject((String)message.getPayload(), FrontMessage.class);
        if (frontMessage.getType().equals("message")){
            log.info("接收到message消息：" + frontMessage.getMine().get("username"));
        }else if (frontMessage.getType().equals("checkOnline")){
            log.info("接收到checkOnline消息：" + frontMessage.getMine().get("username"));
        }else if (frontMessage.getType().equals("addGroup")){
            log.info("接收到addGroup消息：" + frontMessage.getMine().get("username"));
        }else if (frontMessage.getType().equals("changOnline")){
            log.info("接收到changOnline消息：" + frontMessage.getMine().get("username"));
        }else if (frontMessage.getType().equals("addFriend")){
            log.info("接收到addFriend消息：" + frontMessage.getMine().get("username"));
        }else if (frontMessage.getType().equals("agreeAddFriend")){
            log.info("接收到agreeAddFriend消息：" + frontMessage.getMine().get("username"));
        }else if (frontMessage.getType().equals("agreeAddGroup")){
            log.info("接收到agreeAddGroup消息：" + frontMessage.getMine().get("username"));
        }else if (frontMessage.getType().equals("refuseAddGroup")){
            log.info("接收到refuseAddGroup消息：" + frontMessage.getMine().get("username"));
        }else if (frontMessage.getType().equals("unHandMessage")){
            log.info("接收到unHandMessage消息：" + frontMessage.getMine().get("username"));
        }else if (frontMessage.getType().equals("delFriend")){
            log.info("接收到delFriend消息：" + frontMessage.getMine().get("username"));
        }else {
            log.info("接收到其他消息：" + frontMessage.getMine().get("username"));
        }

    }

    /**
     * 发生错误
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // 清除用户缓存信息
    }

    /**
     * 关闭连接
     * @param session
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // 清除用户缓存信息
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}