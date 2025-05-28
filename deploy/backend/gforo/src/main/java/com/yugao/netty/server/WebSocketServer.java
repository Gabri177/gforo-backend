package com.yugao.netty.server;

import com.yugao.netty.initializer.WebSocketChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebSocketServer {

    private final int port = 8081;

    @Autowired
    private WebSocketChannelInitializer webSocketChannelInitializer;

    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup worker = new NioEventLoopGroup();

    @PostConstruct
    public void start() {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(webSocketChannelInitializer);

            Channel channel = bootstrap.bind("0.0.0.0", port).sync().channel();
            System.out.println("✅ Netty WebSocket 服务器启动: ws://localhost:" + port + "/ws");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stop() {
        boss.shutdownGracefully();
        worker.shutdownGracefully();
        System.out.println("🛑 WebSocket服务关闭");
    }

}
