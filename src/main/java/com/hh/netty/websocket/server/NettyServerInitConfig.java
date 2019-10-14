package com.hh.netty.websocket.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.logging.SocketHandler;

/**
 * Netty 服务端初始化配置
 * @author huanh
 * @since 2019-10-14
 */
public class NettyServerInitConfig extends ChannelInitializer<NioSocketChannel> {

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        System.out.println("收到新连接");
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("http-codec", new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536)); // Http消息组装
        pipeline.addLast("http-chunked", new ChunkedWriteHandler()); // WebSocket通信支持
        pipeline.addLast(new NettyServerHandler());//自定义处理类
    }
}
