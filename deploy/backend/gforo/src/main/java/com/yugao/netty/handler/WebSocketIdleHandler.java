package com.yugao.netty.handler;

import com.yugao.netty.registry.ChannelRegistry;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype") // 每次请求创建新的实例
@RequiredArgsConstructor
public class WebSocketIdleHandler extends ChannelInboundHandlerAdapter {

    private final ChannelRegistry channelRegistry;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent e && e.state() == IdleState.READER_IDLE) {
            channelRegistry.removeByChannel(ctx.channel());
            System.out.println("⚠️ 空闲连接，自动断开：" + ctx.channel().id());
            ctx.close();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
