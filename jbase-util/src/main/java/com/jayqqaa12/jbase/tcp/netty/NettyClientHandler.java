package com.jayqqaa12.jbase.tcp.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by 12 on 16-12-06.
 */
public abstract class NettyClientHandler<T> extends SimpleChannelInboundHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(NettyClientHandler.class);

    protected NettyClient client;
    public NettyClientHandler(NettyClient client) {
        this.client = client;
    }

    public NettyClientHandler() {
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("客户端连接断开 触发重连 {} ",ctx.channel());
       if(client!=null)  client.doConnect();
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LOG.info("客户端连接成功 {}",ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, T msg) {
        ctx.channel().attr(AttributeKey.valueOf("heart")).set(System.currentTimeMillis());
        handleMsg(msg);
    }

    protected abstract void handleMsg(T msg);



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) super.exceptionCaught(ctx,cause);
        else LOG.info("异常情况 {}", cause);
    }



}
