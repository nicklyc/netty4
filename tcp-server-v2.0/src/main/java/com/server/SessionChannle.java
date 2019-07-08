package com.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

/**
 * session
 *
 * @author admin
 */
@Component
public class SessionChannle {

    private static volatile Map<ChannelHandlerContext, Map> map = new ConcurrentHashMap<>();

    /**
     * 创建session 每个连接独有
     *
     * @param ctx
     */
    public void creatSession(ChannelHandlerContext ctx) {
        Map session = this.getSession(ctx);
        if (session == null) {
            session = new ConcurrentHashMap<>();
        }
        map.put(ctx, session);
    }


    /**
     * 初始化一个session
     *
     * @param ctx
     */
    public void initSession(ChannelHandlerContext ctx) {
        ConcurrentHashMap<String, ChannelHandlerContext> session = new ConcurrentHashMap<>();
        map.put(ctx, session);
    }

    /**
     * 获取session
     *
     * @param ctx
     * @return
     */
    public Map getSession(ChannelHandlerContext ctx) {
        Map session = map.get(ctx);
        if (null == map.get(ctx)) {
            this.initSession(ctx);
            session = map.get(ctx);
        }
        return session;
    }

    /**
     * session 销毁
     *
     * @param ctx
     */
    public void destorySession(ChannelHandlerContext ctx) {
        map.remove(ctx);
    }

    /**
     * 根据deviceId获取连接
     *
     * @param deviceId
     * @return
     */
    public Channel getChannel(String deviceId) {
        Set<ChannelHandlerContext> keySet = map.keySet();
        Channel channel = null;
        for (ChannelHandlerContext ctx : keySet) {
            Map<String, ChannelHandlerContext> session = map.get(ctx);
            if (session.containsValue(deviceId)) {
                channel = ctx.channel();
            }
        }


        return channel;
    }


    /**
     * 查看所有的在线用户
     *
     * @param deviceId
     * @return
     */
    public List getAllOnline() {
        List online = Lists.newArrayList();

        Set<ChannelHandlerContext> keySet = map.keySet();
        for (ChannelHandlerContext key : keySet) {
            online.add(map.get(key));
        }
        return online;
    }


    private volatile static SessionChannle instance;

    public SessionChannle getInstance() {
        if (instance == null) {
            synchronized (SessionChannle.class) {
                if (instance == null)
                    instance = new SessionChannle();              //instance为volatile，现在没问题了
            }
        }
        return instance;
    }

}
