package com.hh.netty.websocket.manager;

import com.hh.netty.websocket.entity.UserInfo;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.util.StringUtils;
import sun.awt.SunHints;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author huangh
 * @since 2019/10/15
 */
public class ContactManager {
    private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);

    private static ConcurrentMap<Channel, UserInfo> userInfos = new ConcurrentHashMap<>();

    /**
     * 登录注册 channel
     */
    public static void addChannel(Channel channel, String uid) {
        String remoteAddr = channel.remoteAddress().toString();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(uid);
        userInfo.setAddr(remoteAddr);
        userInfo.setChannel(channel);
        userInfos.putIfAbsent(channel, userInfo);
    }

    /**
     * 普通消息
     *
     * @param message
     */
    public static void broadcastMess(String uid, String message, String sender) {
        if (!StringUtils.isEmpty(message)) {
            try {
                rwLock.readLock().lock();
                Set<Channel> keySet = userInfos.keySet();
                for (Channel ch : keySet) {
                    UserInfo userInfo = userInfos.get(ch);
                    if (!userInfo.getUserId().equals(uid)) continue;
                    String backmessage = sender + "," + message;
                    ch.writeAndFlush(new TextWebSocketFrame(backmessage));
                    /*  responseToClient(ch,message);*/
                }
            } finally {
                rwLock.readLock().unlock();
            }
        }
    }

    public static UserInfo getUserInfo(Channel channel) {
        return userInfos.get(channel);
    }

    public static void removeChannel(Channel channel) {
        userInfos.remove(channel);
    }
}
