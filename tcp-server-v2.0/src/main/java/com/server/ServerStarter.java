package com.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 服务启动类
 *
 * @author Administrator
 * @time 2019-01-04
 */
@Component
public class ServerStarter {
    private final static Logger logger = LoggerFactory.getLogger(ServerStarter.class);

    private static final int Size = Runtime.getRuntime().availableProcessors();
    EventLoopGroup bossGroup = new NioEventLoopGroup(Size);
    EventLoopGroup workerGroup = new NioEventLoopGroup(Size);

    public ChannelFuture startServer(String servername, int portNumber, ChannelInitializer initializer) throws InterruptedException {
        try {
            ServerBootstrap boot = new ServerBootstrap();
            //ChannelFuture future =
            boot
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(initializer);
            ChannelFuture future = boot.bind(portNumber);
            logger.info("[" + servername + "]服务启动成功，监听端口==>" + portNumber);
            return future.sync().channel().closeFuture().sync();
        } finally {
            //优雅关机
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
