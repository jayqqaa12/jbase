package com.jayqqaa12.jbase.tcp.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by 12 on 2017/1/23.
 */
public abstract class NettyClient {

    private static final Logger LOG = LoggerFactory.getLogger(NettyClient.class);

    private volatile boolean start;
    protected NioEventLoopGroup workGroup;
    protected Channel channel;
    protected Bootstrap bootstrap;
    private InetSocketAddress addr;


    public void write(Object msg) {
        if (channel != null && channel.isActive()) {
            LOG.info("client send msg {}", msg);
            channel.writeAndFlush(msg);
        }
    }


    private ChannelFutureListener channelFutureListener = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture futureListener) throws Exception {
            if (futureListener.isSuccess()) {
                channel = futureListener.channel();
                LOG.info("Connect to server successfully!");
            } else {
                LOG.info("Failed to connect to server, try connect after 10s {}", futureListener.cause());
                futureListener.channel().eventLoop().schedule(() -> {
                    doConnect();
                }, 10, TimeUnit.SECONDS);
            }
        }
    };


    public void start(ChannelInitializer<SocketChannel> channelInitializer) {
        try {
            start = true;
            LOG.info("start netty client");
            bootstrap = new Bootstrap();
            workGroup = new NioEventLoopGroup(2);
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(channelInitializer);
            config(bootstrap);

            doConnect();

        } catch (Exception e) {
            LOG.info("start netty client fail {}", e);
        }
    }


    protected void config(Bootstrap bootstrap) {

    }


    public void doConnect() {
        if (channel != null && channel.isActive() && !start) return;
        LOG.info("start connect to service  address= {}", addr);
        ChannelFuture future = bootstrap.connect(addr);
        future.addListener(channelFutureListener);
    }


    public void shutdown() throws Exception {
        try {
            start = false;
            if (channel != null && channel.isActive()) channel.close().sync();
            if (workGroup != null) workGroup.shutdownGracefully().sync();

        } catch (Exception e) {
            LOG.warn("shutdown netty client error ...", e);
        }

    }


    public void setAddr(String host, int port) {
        this.addr = InetSocketAddress.createUnresolved(host, port);
    }

    public void setAddr(InetSocketAddress addr) {
        this.addr = addr;
    }

    public InetSocketAddress getAddr() {
        return addr;
    }
}
