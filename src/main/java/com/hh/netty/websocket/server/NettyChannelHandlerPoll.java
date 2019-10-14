package com.hh.netty.websocket.server;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 通道组池，管理所有webSocket连接
 * @author huangh
 * @since 2019/10/14
 */
public class NettyChannelHandlerPoll {
    public NettyChannelHandlerPoll(){}
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
