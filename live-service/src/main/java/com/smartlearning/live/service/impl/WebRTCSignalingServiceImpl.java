package com.smartlearning.live.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlearning.live.dto.WebRTCSignalMessage;
import com.smartlearning.live.service.WebRTCSignalingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * WebRTC信令服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebRTCSignalingServiceImpl implements WebRTCSignalingService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    
    // 房间用户映射：roomId -> userId -> userInfo
    private final Map<String, Map<String, Map<String, Object>>> roomUsers = new ConcurrentHashMap<>();
    
    // 用户会话映射：userId -> WebSocketSession
    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    
    // 会话用户映射：sessionId -> userId
    private final Map<String, String> sessionUsers = new ConcurrentHashMap<>();
    
    @Override
    public void joinRoom(String roomId, String userId, String sessionId, Map<String, Object> userInfo) {
        log.info("用户加入房间: roomId={}, userId={}, sessionId={}", roomId, userId, sessionId);
        
        // 添加用户到房间
        roomUsers.computeIfAbsent(roomId, k -> new ConcurrentHashMap<>())
                .put(userId, userInfo);
        
        // 记录会话映射
        sessionUsers.put(sessionId, userId);
        
        // 缓存到Redis（用于集群环境）
        String roomKey = "live:room:" + roomId + ":users";
        redisTemplate.opsForHash().put(roomKey, userId, userInfo);
        redisTemplate.expire(roomKey, 24, TimeUnit.HOURS);
        
        // 广播用户加入消息
        Map<String, Object> joinMessage = new HashMap<>();
        joinMessage.put("type", "user-joined");
        joinMessage.put("roomId", roomId);
        joinMessage.put("userId", userId);
        joinMessage.put("userInfo", userInfo);
        joinMessage.put("timestamp", System.currentTimeMillis());
        
        broadcastToRoom(roomId, joinMessage, userId);
        
        // 发送房间内现有用户列表给新用户
        List<Map<String, Object>> existingUsers = getRoomUsers(roomId);
        Map<String, Object> usersMessage = new HashMap<>();
        usersMessage.put("type", "room-users");
        usersMessage.put("roomId", roomId);
        usersMessage.put("users", existingUsers);
        
        sendToUser(roomId, userId, usersMessage);
    }
    
    @Override
    public void leaveRoom(String roomId, String userId) {
        log.info("用户离开房间: roomId={}, userId={}", roomId, userId);
        
        // 从房间中移除用户
        Map<String, Map<String, Object>> roomUserMap = roomUsers.get(roomId);
        if (roomUserMap != null) {
            roomUserMap.remove(userId);
            if (roomUserMap.isEmpty()) {
                roomUsers.remove(roomId);
            }
        }
        
        // 从Redis中移除
        String roomKey = "live:room:" + roomId + ":users";
        redisTemplate.opsForHash().delete(roomKey, userId);
        
        // 广播用户离开消息
        Map<String, Object> leaveMessage = new HashMap<>();
        leaveMessage.put("type", "user-left");
        leaveMessage.put("roomId", roomId);
        leaveMessage.put("userId", userId);
        leaveMessage.put("timestamp", System.currentTimeMillis());
        
        broadcastToRoom(roomId, leaveMessage, null);
    }
    
    @Override
    public void handleSignalMessage(WebRTCSignalMessage message) {
        log.debug("处理WebRTC信令消息: type={}, from={}, to={}", 
                message.getType(), message.getFromUserId(), message.getToUserId());
        
        message.setTimestamp(System.currentTimeMillis());
        
        switch (message.getType()) {
            case "offer":
            case "answer":
            case "ice-candidate":
                // 转发信令消息给目标用户
                if (message.getToUserId() != null) {
                    sendToUser(message.getRoomId(), message.getToUserId(), message);
                } else {
                    // 广播给房间内其他用户
                    broadcastToRoom(message.getRoomId(), message, message.getFromUserId());
                }
                break;
            case "join":
                handleJoinSignal(message);
                break;
            case "leave":
                handleLeaveSignal(message);
                break;
            default:
                log.warn("未知的信令消息类型: {}", message.getType());
        }
    }
    
    @Override
    public List<Map<String, Object>> getRoomUsers(String roomId) {
        Map<String, Map<String, Object>> roomUserMap = roomUsers.get(roomId);
        if (roomUserMap != null) {
            return new ArrayList<>(roomUserMap.values());
        }
        
        // 从Redis获取（集群环境）
        String roomKey = "live:room:" + roomId + ":users";
        Map<Object, Object> redisUsers = redisTemplate.opsForHash().entries(roomKey);
        
        List<Map<String, Object>> users = new ArrayList<>();
        for (Object userInfo : redisUsers.values()) {
            if (userInfo instanceof Map) {
                users.add((Map<String, Object>) userInfo);
            }
        }
        
        return users;
    }
    
    @Override
    public Map<String, Object> getRoomStats(String roomId) {
        List<Map<String, Object>> users = getRoomUsers(roomId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("roomId", roomId);
        stats.put("userCount", users.size());
        stats.put("users", users);
        stats.put("timestamp", System.currentTimeMillis());
        
        return stats;
    }
    
    @Override
    public void broadcastToRoom(String roomId, Object message, String excludeUserId) {
        Map<String, Map<String, Object>> roomUserMap = roomUsers.get(roomId);
        if (roomUserMap == null) {
            return;
        }
        
        String messageStr;
        try {
            messageStr = objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            log.error("序列化消息失败", e);
            return;
        }
        
        roomUserMap.keySet().forEach(userId -> {
            if (!userId.equals(excludeUserId)) {
                WebSocketSession session = userSessions.get(userId);
                if (session != null && session.isOpen()) {
                    try {
                        session.sendMessage(new TextMessage(messageStr));
                    } catch (IOException e) {
                        log.error("发送消息失败: userId={}", userId, e);
                        // 清理断开的连接
                        userSessions.remove(userId);
                    }
                }
            }
        });
    }
    
    @Override
    public void sendToUser(String roomId, String userId, Object message) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                String messageStr = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(messageStr));
            } catch (IOException e) {
                log.error("发送消息给用户失败: userId={}", userId, e);
                userSessions.remove(userId);
            }
        }
    }
    
    @Override
    public boolean isUserInRoom(String roomId, String userId) {
        Map<String, Map<String, Object>> roomUserMap = roomUsers.get(roomId);
        return roomUserMap != null && roomUserMap.containsKey(userId);
    }
    
    @Override
    public List<String> getUserRooms(String userId) {
        List<String> userRooms = new ArrayList<>();
        
        for (Map.Entry<String, Map<String, Map<String, Object>>> entry : roomUsers.entrySet()) {
            if (entry.getValue().containsKey(userId)) {
                userRooms.add(entry.getKey());
            }
        }
        
        return userRooms;
    }
    
    @Override
    public void cleanupDisconnectedUser(String sessionId) {
        String userId = sessionUsers.remove(sessionId);
        if (userId != null) {
            userSessions.remove(userId);
            
            // 从所有房间中移除用户
            List<String> userRooms = getUserRooms(userId);
            for (String roomId : userRooms) {
                leaveRoom(roomId, userId);
            }
        }
    }
    
    /**
     * 处理加入信令
     */
    private void handleJoinSignal(WebRTCSignalMessage message) {
        // 通知房间内其他用户有新用户加入
        Map<String, Object> joinNotification = new HashMap<>();
        joinNotification.put("type", "peer-joined");
        joinNotification.put("userId", message.getFromUserId());
        joinNotification.put("roomId", message.getRoomId());
        
        broadcastToRoom(message.getRoomId(), joinNotification, message.getFromUserId());
    }
    
    /**
     * 处理离开信令
     */
    private void handleLeaveSignal(WebRTCSignalMessage message) {
        // 通知房间内其他用户有用户离开
        Map<String, Object> leaveNotification = new HashMap<>();
        leaveNotification.put("type", "peer-left");
        leaveNotification.put("userId", message.getFromUserId());
        leaveNotification.put("roomId", message.getRoomId());
        
        broadcastToRoom(message.getRoomId(), leaveNotification, null);
    }
    
    /**
     * 注册用户会话
     */
    public void registerUserSession(String userId, WebSocketSession session) {
        userSessions.put(userId, session);
    }
    
    /**
     * 注销用户会话
     */
    public void unregisterUserSession(String userId) {
        userSessions.remove(userId);
    }
}
