package com.jayqqaa12.jbase.tcp.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class NettyServer {
    private Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private int port;
    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public NettyServer(int port) {
        this.port = port;
    }

    public void startServer() {

        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    addChannelHandler(pipeline);
                }
            });
            channel = b.bind(port).sync().channel();
            logger.info("行情发送服务器 初始化完成");

        } catch (InterruptedException e) {
            logger.warn("行情发送服务器 出错...", e);
        }
    }


    protected abstract void addChannelHandler(ChannelPipeline pipeline);


    public void destroy() throws Exception {
        try {


            if (channel != null && channel.isActive()) {
                channel.close().sync();
            }
            if (bossGroup != null) {
                bossGroup.shutdownGracefully().syncUninterruptibly();
            }
            if (workerGroup != null) {
                workerGroup.shutdownGracefully().syncUninterruptibly();
            }

        } catch (Exception e) {
            logger.warn("行情发送服务器 关闭出错...", e);
        }
    }
}
