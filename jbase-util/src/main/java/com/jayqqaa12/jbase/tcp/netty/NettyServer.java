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
            config(b);

            channel = b.bind(port).sync().channel();
            logger.info(" netty server port {} start ", port);

        } catch (InterruptedException e) {
            logger.warn(" netty server start {} error", port, e);
        }
    }

    protected void config(ServerBootstrap bootstrap) {

    }


    protected abstract void addChannelHandler(ChannelPipeline pipeline);


    public void destroy() {
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
            logger.warn("netty destory error...", e);
        }
    }
}
