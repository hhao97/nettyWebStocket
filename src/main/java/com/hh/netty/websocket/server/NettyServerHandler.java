package com.hh.netty.websocket.server;

import com.alibaba.fastjson.JSON;
import com.hh.netty.websocket.entity.Message;
import com.hh.netty.websocket.manager.ContactManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.io.UnsupportedEncodingException;

/**
 * @author huangh
 * @since 2019/10/14
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    public static ChannelGroup channels = NettyChannelHandlerPoll.channelGroup;
    private WebSocketServerHandshaker handShaker;
    private final String wsUri = "/ws";
    //websocket握手升级绑定页面
    String wsFactoryUri = "";

    /*
     * 握手建立
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        channels.add(incoming);
    }

    /*
     * 握手取消
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        channels.remove(incoming);
        ContactManager.removeChannel(ctx.channel());
    }

    /*
     * channelAction
     *
     * channel 通道 action 活跃的
     *
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。
     *
     */
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().localAddress().toString() + " 通道已激活！");
    }

    /*
     * channelInactive
     *
     * channel 通道 Inactive 不活跃的
     *
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。
     *
     */
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().localAddress().toString() + " 通道不活跃！");
    }

    /*
     * 功能：读取 h5页面发送过来的信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {// 如果是HTTP请求，进行HTTP操作
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {// 如果是Websocket请求，则进行websocket操作
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    /*
     * 功能：读空闲时移除Channel
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent evnet = (IdleStateEvent) evt;
            // 判断Channel是否读空闲, 读空闲时移除Channel
            if (evnet.state().equals(IdleState.READER_IDLE)) {
                ContactManager.removeChannel(ctx.channel());
            }
        }
        ctx.fireUserEventTriggered(evt);
    }

    /*
     * 功能：处理HTTP的代码
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws UnsupportedEncodingException {
        // 如果HTTP解码失败，返回HHTP异常
        if (req instanceof HttpRequest) {
            HttpMethod method = req.getMethod();
            // 如果是websocket请求就握手升级
            if (wsUri.equalsIgnoreCase(req.getUri())) {
                WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                        wsFactoryUri, null, false);
                handShaker = wsFactory.newHandshaker(req);
                if (handShaker == null) {
                    WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
                } else {
                    handShaker.handshake(ctx.channel(), req);
                }
            }

        }
    }

    /*
     * 处理Websocket的代码
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否是关闭链路的指令
        //log.info("websocket get");
        if (frame instanceof CloseWebSocketFrame) {
            handShaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 文本消息，不支持二进制消息
        if (frame instanceof TextWebSocketFrame) {
            // 返回应答消息
            String requestmsg = ((TextWebSocketFrame) frame).text();
            // 登录请求 验证是请求数据是否是数字 是的话就登录
            if (requestmsg.matches("^[1-9]\\d*$")) {
                ContactManager.addChannel(ctx.channel(), requestmsg);
            } else {
                //登录失败 逻辑
            }
        }
    }

    /**
     * 功能：服务端发生异常的操作
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
