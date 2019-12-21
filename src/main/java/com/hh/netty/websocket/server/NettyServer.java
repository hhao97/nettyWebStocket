package com.hh.netty.websocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Netty 服务端启动类
 *
 * @author huangh
 * @since 2019-10-14
 */
@Slf4j
public class NettyServer {
    /**
     * 端口号
     */
    private static Integer PORT = 7890;
    /**
     * 端口号最大值
     */
    private static Integer MAX_PORT = 7900;

    public static void run() throws InterruptedException {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.group(workerGroup, bossGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new NettyServerInitConfig());
            ChannelFuture cf = bind(bootstrap);
            System.out.println(NettyServer.class + " 启动正在监听： " + cf.channel().localAddress());
            cf.channel().closeFuture().sync(); // 关闭服务器通道
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("启动服务端出错,原因={}"+e.getMessage());
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

}

    private static ChannelFuture bind(final ServerBootstrap serverBootstrap) throws InterruptedException {
        return serverBootstrap.bind(PORT).addListener(future -> {
            if (future.isSuccess())
                System.out.println("netty 服务端口绑定成功, 当前端口号={}"+PORT);
            else if (Objects.equals(PORT, MAX_PORT)) {
                System.out.println("netty 服务端端口绑定失败");
                //TODO
            } else {
                PORT++;
                bind(serverBootstrap);
            }
        }).sync();
    }
}
