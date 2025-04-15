package com.ninetrees.musicapp.service;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Classname:WebSocketService
 * Description:
 */
@ServerEndpoint("/ws")
public interface WebSocketService {
    @OnMessage
    public default void onMessage(Session session, String message) {
        // 处理接收到的消息
        // 例如控制播放，广播给其他客户端
    }

    @OnClose
    public default void onClose(Session session) {
        // 关闭时处理
    }

}
