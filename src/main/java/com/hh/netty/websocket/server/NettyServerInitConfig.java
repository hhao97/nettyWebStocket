package com.hh.netty.websocket.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Netty 服务端初始化配置
 * @author huanh
 * @since 2019-10-14
 */
public class NettyServerInitConfig extends ChannelInitializer<NioSocketChannel> {

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        System.out.println("收到新连接");
        //webSocket协议本身是基于http协议的，所以这边也要使用http解编码器
        ch.pipeline().addLast(new HttpServerCodec());
        //以块的方式来写的处理器
        ch.pipeline().addLast(new ChunkedWriteHandler());
        ch.pipeline().addLast(new HttpObjectAggregator(65536));
        ch.pipeline().addLast(new NettyServerHandler());
        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws", null, true, 65536 * 10));
    }
}
