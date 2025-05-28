package com.yugao.netty.initializer;

import com.yugao.netty.handler.WebSocketHandler;
import com.yugao.netty.handler.WebSocketHandshakeHandler;
import com.yugao.netty.handler.WebSocketIdleHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new HttpServerCodec());
        p.addLast(new HttpObjectAggregator(65536));
        p.addLast(new IdleStateHandler(60, 0, 0)); // 60秒未读触发事件
        p.addLast(applicationContext.getBean(WebSocketIdleHandler.class));
        p.addLast(applicationContext.getBean(WebSocketHandshakeHandler.class));
        p.addLast(applicationContext.getBean(WebSocketHandler.class));
    }
}
