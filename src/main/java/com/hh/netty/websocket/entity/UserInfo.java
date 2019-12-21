package com.hh.netty.websocket.entity;

import io.netty.channel.Channel;

/**
 * @author huangh
 * @since 2019/10/15
 */
public class UserInfo {
    private String userId;  // UID
    private String addr;    // 地址
    private Channel channel;// 通道

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
