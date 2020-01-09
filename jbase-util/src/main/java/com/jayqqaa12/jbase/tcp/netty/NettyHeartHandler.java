package com.jayqqaa12.jbase.tcp.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 12 on 2017/1/11.
 * <p>
 * 需要配合 IdleStateHandler 使用
 */
public class NettyHeartHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(NettyHeartHandler.class);
    private final HeartMsg heartMsg;
    private final Integer timeoutSecond ;

    public NettyHeartHandler(HeartMsg heartMsg,Integer timeoutSecond) {

        this.timeoutSecond=timeoutSecond;

        this.heartMsg = heartMsg;
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //处理超时未收到消息的事件
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    onIdeled(ctx);
                    break;
                case WRITER_IDLE:

                    break;
                case ALL_IDLE:

                    break;
            }
        } else super.userEventTriggered(ctx, evt);

    }


    private void onIdeled(ChannelHandlerContext ctx) {

        Long lastTime = (Long) ctx.channel().attr(AttributeKey.valueOf("heart")).get();

        if (lastTime != null && System.currentTimeMillis() - lastTime >= timeoutSecond * 1000) {
            LOG.info("wait for heart timeout close the channel {}", ctx);
            ctx.channel().attr(AttributeKey.valueOf("heart")).set(null);
            ctx.close();
        } else {
            LOG.info("send heart .. {}", ctx);
            if (lastTime == null) ctx.channel().attr(AttributeKey.valueOf("heart")).set(System.currentTimeMillis());
            ctx.writeAndFlush(heartMsg.msg());
        }
    }

    @FunctionalInterface
    public interface HeartMsg<T> {
        T msg();
    }

}

