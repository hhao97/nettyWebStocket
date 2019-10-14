package com.hh.netty.websocket.entity;

import io.netty.channel.Channel;
import lombok.Data;

/**
 * @author huangh
 * @since 2019/10/15
 */
@Data
public class UserInfo {
    private String userId;  // UID
    private String addr;    // 地址
    private Channel channel;// 通道
}
