package com.revers.rec.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

@EnableWebSocket()
@Configuration
public class webSocketConfig implements WebSocketConfigurer {

    @Resource
    WebSocketHandler defaultHandler;

    @Resource
    DefaultInterceptor defaultInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(defaultHandler,"/websocket/")
                .addInterceptors(defaultInterceptor)
                .setAllowedOrigins("*");
    }
}