package com.ninetrees.musicapp.service; // 包名根据你的项目调整

import com.ninetrees.musicapp.entity.Room;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomManager {
    // 存储所有房间，Key 是 RoomID
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    // 存储 Session ID 到 RoomID 的映射，方便用户离开时查找房间
    private final Map<String, String> sessionIdToRoomId = new ConcurrentHashMap<>();

    // 创建新房间
    public synchronized Room createRoom() {
        String roomId = UUID.randomUUID().toString().substring(0, 8); // 生成唯一 ID
        Room newRoom = new Room(roomId);
        rooms.put(roomId, newRoom);
        System.out.println("房间创建成功: " + roomId);
        return newRoom;
    }

    // 加入房间
    public Room joinRoom(WebSocketSession session, String roomId) {
        Room room = rooms.get(roomId);
        if (room != null) {
            if (room.addMember(session)) {
                sessionIdToRoomId.put(session.getId(), roomId);
                System.out.println("用户 " + session.getId() + " 加入房间: " + roomId);
                return room;
            }
        } else {
            System.err.println("尝试加入不存在的房间: " + roomId);
        }
        return null; // 加入失败或房间不存在
    }

    // 离开房间
    public Room leaveRoom(WebSocketSession session) {
        String roomId = sessionIdToRoomId.remove(session.getId());
        if (roomId != null) {
            Room room = rooms.get(roomId);
            if (room != null) {
                if(room.removeMember(session)) {
                    System.out.println("用户 " + session.getId() + " 离开房间: " + roomId);
                    // 如果房间空了，可以选择销毁房间
                    if (room.getMembers().isEmpty()) {
                        rooms.remove(roomId);
                        System.out.println("房间 " + roomId + " 已空，销毁。");
                    }
                    return room; // 返回用户离开的那个房间，可能需要通知其他人
                }
            }
        }
        return null;
    }

    // 根据 Session 获取房间
    public Room getRoomBySession(WebSocketSession session) {
        String roomId = sessionIdToRoomId.get(session.getId());
        if (roomId != null) {
            return rooms.get(roomId);
        }
        return null;
    }

    // 根据 Room ID 获取房间
    public Room getRoomById(String roomId) {
        return rooms.get(roomId);
    }
}