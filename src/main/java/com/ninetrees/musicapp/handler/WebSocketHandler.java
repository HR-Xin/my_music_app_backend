package com.ninetrees.musicapp.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode; // 需要引入 Jackson 库
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninetrees.musicapp.entity.Room;
import com.ninetrees.musicapp.service.RoomManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap; // 使用 ConcurrentHashMap

//@Component // 确保 Spring Boot 能扫描到，或者在 WebSocketConfig 中手动注册
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    @Autowired // 注入 RoomManager
    private RoomManager roomManager;
    // 使用 ConcurrentHashMap 存储 session，线程安全
    private final ObjectMapper objectMapper = new ObjectMapper(); // 用于 JSON 操作

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session); // 使用 session ID 作为 key
        System.out.println("New WebSocket connection established: " + session.getId());
        // 可以考虑在这里要求客户端发送用户信息进行注册
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received message from " + session.getId() + ": " + payload);

        try {
            // 解析 JSON 消息
            JsonNode jsonNode = objectMapper.readTree(payload);
            String type = jsonNode.has("type") ? jsonNode.get("type").asText() : null;

            switch (type) {
                case "join":
                    handleJoin(session, jsonNode);
                    break;
                case "chat":
                    handleChat(session, jsonNode);
                    break;
                case "playback":
                    handlePlayback(session, jsonNode);
                    break;
                case "change_song": // 处理切换歌曲（由文件上传后触发）
                    handleChangeSong(session, jsonNode);
                    break;
                default:
                    System.out.println("Unknown message type: " + type);
            }
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing JSON message from " + session.getId() + ": " + payload);
            // 可以向客户端发送错误消息
            // session.sendMessage(new TextMessage("{\"type\":\"error\", \"message\":\"Invalid JSON format\"}"));
        } catch (Exception e) {
            System.err.println("Error handling message from " + session.getId() + ": " + payload);
            e.printStackTrace();
            // 可以向客户端发送错误消息
            // session.sendMessage(new TextMessage("{\"type\":\"error\", \"message\":\"Server error handling message\"}"));
        }
    }

    // 处理加入房间
    private void handleJoin(WebSocketSession session, JsonNode jsonNode) throws IOException {
        String roomId = jsonNode.path("roomId").asText(null);
        String username = jsonNode.path("username").asText("匿名用户"); // 获取用户名

        if (roomId == null) {
            System.err.println("Join message from " + session.getId() + " missing roomId.");
            session.sendMessage(new TextMessage("{\"type\":\"error\", \"message\":\"Missing roomId for join\"}"));
            return;
        }

        Room room = roomManager.joinRoom(session, roomId);
        if (room != null) {
            // 1. 通知房间内其他成员有人加入
            Map<String, String> joinMsg = new HashMap<>();
            joinMsg.put("type", "user_join");
            joinMsg.put("userId", session.getId()); // 可以用 Session ID 或更好的用户标识
            joinMsg.put("username", username); // 加入的用户名
            broadcastMessage(room, session, new TextMessage(objectMapper.writeValueAsString(joinMsg)));

            // 2. (重要) 将当前房间的播放状态发送给新加入者
            Map<String, Object> currentState = room.getCurrentPlaybackState();
            System.out.println("Sending initial state to " + session.getId() + ": " + objectMapper.writeValueAsString(currentState));
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(currentState)));

            // 3. (可选) 将当前在线用户列表发给新加入者
            // ...
        } else {
            System.err.println("User " + session.getId() + " failed to join room " + roomId);
            session.sendMessage(new TextMessage("{\"type\":\"error\", \"message\":\"Failed to join room or room not found\"}"));
            session.close(); // 加入失败，关闭连接
        }
    }

    // 处理聊天消息
    private void handleChat(WebSocketSession session, JsonNode jsonNode) throws IOException {
        Room room = roomManager.getRoomBySession(session);
        if (room != null) {
            String text = jsonNode.path("text").asText();
            String username = jsonNode.path("username").asText("匿名用户"); // 获取用户名

            // 构建要广播的消息，包含发送者信息
            Map<String, String> chatMsg = new HashMap<>();
            chatMsg.put("type", "chat");
            chatMsg.put("senderId", session.getId()); // 唯一标识符
            chatMsg.put("sender", username); // 发送者名字
            chatMsg.put("text", text);

            // 广播给房间内所有人（包括自己，以便确认）
            broadcastMessage(room, null, new TextMessage(objectMapper.writeValueAsString(chatMsg)));
        } else {
            System.err.println("Chat message from user " + session.getId() + " who is not in a room.");
            // session.sendMessage(new TextMessage("{\"type\":\"error\", \"message\":\"You are not in a room\"}"));
        }
    }

    // 处理播放状态同步
    private void handlePlayback(WebSocketSession session, JsonNode jsonNode) throws IOException {
        Room room = roomManager.getRoomBySession(session);
        if (room != null) {
            boolean isPlaying = jsonNode.path("isPlaying").asBoolean();
            double currentTime = jsonNode.path("currentTime").asDouble();
            // String songUrl = jsonNode.path("songUrl").asText(null); // 通常播放控制不主动带 URL

            // 更新房间的共享状态
            // 注意：这里直接信任客户端的时间可能导致不同步，更健壮的做法是以服务器时间或某个成员的时间为准
            room.setPlaying(isPlaying);
            room.setCurrentTime(currentTime);
            // if (songUrl != null) room.setCurrentSongUrl(songUrl); // 通常由 change_song 处理

            // 构建要广播的状态消息
            Map<String, Object> playbackMsg = new HashMap<>();
            playbackMsg.put("type", "playback");
            playbackMsg.put("isPlaying", isPlaying);
            playbackMsg.put("currentTime", currentTime);
            // playbackMsg.put("songUrl", room.getCurrentSongUrl()); // 可以带上当前 URL

            // 广播给房间内其他成员
            broadcastMessage(room, session, new TextMessage(objectMapper.writeValueAsString(playbackMsg)));
        } else {
            System.err.println("Playback message from user " + session.getId() + " who is not in a room.");
        }
    }

    // 处理切换歌曲
    private void handleChangeSong(WebSocketSession session, JsonNode jsonNode) throws IOException {
        Room room = roomManager.getRoomBySession(session);
        if (room != null) {
            String songUrl = jsonNode.path("songUrl").asText(null);
            String songTitle = jsonNode.path("songTitle").asText("未知歌曲");
            String songCover = jsonNode.path("songCover").asText(null);
            String songArtist = jsonNode.path("songArtist").asText(null);

            if (songUrl != null) {
                // 更新房间的当前歌曲信息和播放状态
                room.setCurrentSongUrl(songUrl);
                room.setCurrentSongTitle(songTitle);
                room.setCurrentSongCover(songCover);
                room.setPlaying(true); // 切换歌曲后通常自动播放
                room.setCurrentTime(0.0); // 从头开始

                // 构建切换歌曲消息
                Map<String, Object> changeMsg = new HashMap<>();
                changeMsg.put("type", "change_song"); // 使用特定类型或复用 playback/initial_state
                changeMsg.put("songUrl", songUrl);
                changeMsg.put("songTitle", songTitle);
                changeMsg.put("songArtist", songArtist);
                changeMsg.put("songCover", songCover);
                changeMsg.put("isPlaying", true);
                changeMsg.put("currentTime", 0.0);

                // 广播给房间内所有人
                broadcastMessage(room, null, new TextMessage(objectMapper.writeValueAsString(changeMsg)));
                System.out.println("Room " + room.getRoomId() + " changed song to: " + songTitle);
            } else {
                System.err.println("Change song message missing songUrl from " + session.getId());
            }
        } else {
            System.err.println("Change song message from user " + session.getId() + " who is not in a room.");
        }
    }

    // 广播消息给除了发送者之外的所有会话
    // 广播消息
    // senderSession 为 null 时广播给所有人，否则广播给除 sender 外的所有人
    private void broadcastMessage(Room room, WebSocketSession senderSession, TextMessage message) {
        if (room == null) return;
        String senderId = (senderSession != null) ? senderSession.getId() : null;

        System.out.println("Broadcasting to room " + room.getRoomId() + " (exclude sender: " + (senderId != null) + "): " + message.getPayload());

        room.getMembers().forEach(session -> {
            // 检查 session 是否打开，并且是否需要排除发送者
            if (session.isOpen() && (senderId == null || !session.getId().equals(senderId))) {
                System.out.println("Sending to session: " + session.getId());
                try {
                    // 使用 synchronized 避免 WebSocketSession 的并发写入问题
                    synchronized (session) {
                        session.sendMessage(message);
                    }
                } catch (IOException e) {
                    System.err.println("Error sending message to session " + session.getId());
                    e.printStackTrace();
                    // 考虑移除发送失败的 session
                    // roomManager.leaveRoom(session); // 这可能会触发并发修改异常，需要小心处理
                }
            } else if (!session.isOpen()) {
                System.out.println("Session " + session.getId() + " is closed, skipping broadcast.");
                // 在这里移除可能更安全
                // roomManager.leaveRoom(session);
            } else if (senderId != null && session.getId().equals(senderId)) {
                System.out.println("Skipping sender session: " + session.getId());
            }
        });
    }
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println(">>> Transport Error for " + session.getId() + ": " + exception.getMessage());
        exception.printStackTrace();
        // 移除并通知其他人
        Room room = roomManager.leaveRoom(session);
        if (room != null) {
            Map<String, String> leaveMsg = new HashMap<>();
            leaveMsg.put("type", "user_leave");
            leaveMsg.put("userId", session.getId());
            broadcastMessage(room, session, new TextMessage(objectMapper.writeValueAsString(leaveMsg)));
        }
        // 关闭可能已损坏的连接
        // if (session.isOpen()) { session.close(CloseStatus.SERVER_ERROR); }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println(">>> Connection Closed: " + session.getId() + " Status: " + status);
        // 移除 Session 并通知房间内其他人
        Room room = roomManager.leaveRoom(session);
        if (room != null) {
            System.out.println("Broadcasting user leave for " + session.getId() + " in room " + room.getRoomId());
            Map<String, String> leaveMsg = new HashMap<>();
            leaveMsg.put("type", "user_leave");
            leaveMsg.put("userId", session.getId());
            // leaveMsg.put("username", "用户名"); // 可以尝试从 session attributes 获取用户名
            broadcastMessage(room, session, new TextMessage(objectMapper.writeValueAsString(leaveMsg)));
        } else {
            System.out.println("Session " + session.getId() + " was not found in any room upon closing.");
        }
    }
}