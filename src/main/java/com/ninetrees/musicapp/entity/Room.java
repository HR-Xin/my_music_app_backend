package com.ninetrees.musicapp.entity;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Classname:Room
 * Description:
 */
@Data
public class Room {
    private final String roomId;
    // 使用线程安全的 Set 存储会话
    private final Set<WebSocketSession> members = new CopyOnWriteArraySet<>();
    // 存储当前房间的播放状态 (简化示例)
    private String currentSongUrl = null;
    private String currentSongTitle = "未知歌曲"; // 添加标题等信息
    private String currentSongCover = null;
    private String currentSongAuthor = null;
    private boolean isPlaying = false;
    private double currentTime = 0.0;

    public Room(String roomId) {
        this.roomId = roomId;
    }

    public boolean addMember(WebSocketSession session) {
        return members.add(session);
    }

    public boolean removeMember(WebSocketSession session) {
        return members.remove(session);
    }

    // 可以添加获取当前完整播放状态的方法
    public synchronized Map<String, Object> getCurrentPlaybackState() {
        Map<String, Object> state = new HashMap<>();
        state.put("type", "initial_state"); // 或 "playback_state"
        state.put("songUrl", currentSongUrl);
        state.put("songTitle", currentSongTitle);
        state.put("songCover", currentSongCover);
        state.put("songAuthor", currentSongAuthor);
        state.put("isPlaying", isPlaying);
        state.put("currentTime", currentTime);
        // 如果需要，还可以包含房间成员列表等信息
        return state;
    }

}
