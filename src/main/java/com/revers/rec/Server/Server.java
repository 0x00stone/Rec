/*
package com.revers.rec.Server;


import com.revers.rec.Server.Handler.HandShakeHandler;
import com.revers.rec.Server.Handler.HeartBeatHandler;
import com.revers.rec.Server.Handler.ServerHandler;
import com.revers.rec.config.AccountConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.ServerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Server {

    @Autowired
    AccountConfig config;

    public void start() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap boot = new ServerBootstrap();
        boot.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO)) // 给boss添加Handler
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new ObjectDecoder(1024 * 1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                        channel.pipeline().addLast(new ObjectEncoder());
                        channel.pipeline().addLast("readTimeoutHandler",new ReadTimeoutHandler(20));

                        channel.pipeline().addLast(new HandShakeHandler());
                        channel.pipeline().addLast(new HeartBeatHandler());
                        channel.pipeline().addLast(new ServerHandler());
                    }
                }).option(ChannelOption.SO_BACKLOG, 100)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        //ServerInfo serverInfo = new ServerInfo("127.0.0.1", new Integer(8080), config.getIpv6Port());
        //log.info("[+] 服务端上线..." + serverInfo.toString());
        try {
            ChannelFuture future = boot.bind("127.0.0.1", config.getIpv6Port()).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {

        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
*/
