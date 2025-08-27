package com.smartlearning.live.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlearning.live.dto.WebRTCSignalMessage;
import com.smartlearning.live.service.WebRTCSignalingService;
import com.smartlearning.live.service.impl.WebRTCSignalingServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 直播WebSocket处理器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LiveWebSocketHandler implements WebSocketHandler {

    private final ObjectMapper objectMapper;
    private final WebRTCSignalingService signalingService;

    // 存储房间和连接的映射关系
    private final Map<String, Map<String, WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = extractRoomId(session);
        String userId = extractUserId(session);

        log.info("WebSocket连接建立: roomId={}, userId={}, sessionId={}", roomId, userId, session.getId());

        // 将会话添加到房间
        roomSessions.computeIfAbsent(roomId, k -> new ConcurrentHashMap<>()).put(userId, session);

        // 注册到信令服务
        if (signalingService instanceof WebRTCSignalingServiceImpl) {
            ((WebRTCSignalingServiceImpl) signalingService).registerUserSession(userId, session);
        }

        // 构建用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        userInfo.put("sessionId", session.getId());
        userInfo.put("joinTime", System.currentTimeMillis());

        // 加入房间
        signalingService.joinRoom(roomId, userId, session.getId(), userInfo);

        // 发送欢迎消息
        sendWelcomeMessage(session, roomId);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String roomId = extractRoomId(session);
        String userId = extractUserId(session);

        log.info("收到WebSocket消息: roomId={}, userId={}, message={}", roomId, userId, message.getPayload());

        try {
            // 解析消息
            Map<String, Object> messageData = objectMapper.readValue(message.getPayload().toString(), Map.class);
            String messageType = (String) messageData.get("type");

            switch (messageType) {
                case "chat":
                    handleChatMessage(roomId, userId, messageData);
                    break;
                case "heartbeat":
                    handleHeartbeat(session);
                    break;
                case "webrtc-signal":
                    handleWebRTCSignal(messageData);
                    break;
                case "signal":
                    handleSignalMessage(roomId, userId, messageData);
                    break;
                default:
                    log.warn("未知消息类型: {}", messageType);
            }
        } catch (Exception e) {
            log.error("处理WebSocket消息失败", e);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket传输错误: sessionId={}", session.getId(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String roomId = extractRoomId(session);
        String userId = extractUserId(session);

        log.info("WebSocket连接关闭: roomId={}, userId={}, sessionId={}, status={}",
                roomId, userId, session.getId(), closeStatus);

        // 从房间中移除会话
        Map<String, WebSocketSession> roomSessionMap = roomSessions.get(roomId);
        if (roomSessionMap != null) {
            roomSessionMap.remove(userId);
            if (roomSessionMap.isEmpty()) {
                roomSessions.remove(roomId);
            }
        }

        // 从信令服务中移除
        if (signalingService instanceof WebRTCSignalingServiceImpl) {
            ((WebRTCSignalingServiceImpl) signalingService).unregisterUserSession(userId);
        }

        // 离开房间
        signalingService.leaveRoom(roomId, userId);

        // 清理断开连接的用户
        signalingService.cleanupDisconnectedUser(session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 从会话中提取房间ID
     */
    private String extractRoomId(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri != null) {
            String path = uri.getPath();
            String[] parts = path.split("/");
            if (parts.length > 0) {
                return parts[parts.length - 1];
            }
        }
        return "unknown";
    }

    /**
     * 从会话中提取用户ID
     */
    private String extractUserId(WebSocketSession session) {
        // 从查询参数或头部获取用户ID
        URI uri = session.getUri();
        if (uri != null && uri.getQuery() != null) {
            String[] params = uri.getQuery().split("&");
            for (String param : params) {
                String[] kv = param.split("=");
                if (kv.length == 2 && "userId".equals(kv[0])) {
                    return kv[1];
                }
            }
        }
        return "anonymous";
    }

    /**
     * 发送欢迎消息
     */
    private void sendWelcomeMessage(WebSocketSession session, String roomId) {
        try {
            Map<String, Object> welcomeMsg = Map.of(
                    "type", "welcome",
                    "roomId", roomId,
                    "message", "欢迎进入直播间");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(welcomeMsg)));
        } catch (IOException e) {
            log.error("发送欢迎消息失败", e);
        }
    }

    /**
     * 处理聊天消息
     */
    private void handleChatMessage(String roomId, String userId, Map<String, Object> messageData) {
        // 广播聊天消息到房间内所有用户
        Map<String, Object> chatMsg = Map.of(
                "type", "chat",
                "roomId", roomId,
                "userId", userId,
                "content", messageData.get("content"),
                "timestamp", System.currentTimeMillis());
        broadcastToRoom(roomId, chatMsg);
    }

    /**
     * 处理心跳消息
     */
    private void handleHeartbeat(WebSocketSession session) {
        try {
            Map<String, Object> pongMsg = Map.of("type", "pong");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(pongMsg)));
        } catch (IOException e) {
            log.error("发送心跳响应失败", e);
        }
    }

    /**
     * 处理WebRTC信令消息
     */
    private void handleWebRTCSignal(Map<String, Object> messageData) {
        try {
            WebRTCSignalMessage signalMessage = objectMapper.convertValue(messageData, WebRTCSignalMessage.class);
            signalingService.handleSignalMessage(signalMessage);
        } catch (Exception e) {
            log.error("处理WebRTC信令消息失败", e);
        }
    }

    /**
     * 处理信令消息（WebRTC）
     */
    private void handleSignalMessage(String roomId, String userId, Map<String, Object> messageData) {
        // 转发信令消息给目标用户
        String targetUserId = (String) messageData.get("targetUserId");
        if (targetUserId != null) {
            sendToUser(roomId, targetUserId, messageData);
        }
    }

    /**
     * 广播用户进入消息
     */
    private void broadcastUserJoined(String roomId, String userId) {
        Map<String, Object> joinMsg = Map.of(
                "type", "userJoined",
                "roomId", roomId,
                "userId", userId,
                "timestamp", System.currentTimeMillis());
        broadcastToRoom(roomId, joinMsg);
    }

    /**
     * 广播用户离开消息
     */
    private void broadcastUserLeft(String roomId, String userId) {
        Map<String, Object> leaveMsg = Map.of(
                "type", "userLeft",
                "roomId", roomId,
                "userId", userId,
                "timestamp", System.currentTimeMillis());
        broadcastToRoom(roomId, leaveMsg);
    }

    /**
     * 向房间内所有用户广播消息
     */
    private void broadcastToRoom(String roomId, Map<String, Object> message) {
        Map<String, WebSocketSession> roomSessionMap = roomSessions.get(roomId);
        if (roomSessionMap != null) {
            String messageStr;
            try {
                messageStr = objectMapper.writeValueAsString(message);
            } catch (Exception e) {
                log.error("序列化消息失败", e);
                return;
            }

            roomSessionMap.values().forEach(session -> {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(new TextMessage(messageStr));
                    }
                } catch (IOException e) {
                    log.error("发送消息失败", e);
                }
            });
        }
    }

    /**
     * 向特定用户发送消息
     */
    private void sendToUser(String roomId, String userId, Map<String, Object> message) {
        Map<String, WebSocketSession> roomSessionMap = roomSessions.get(roomId);
        if (roomSessionMap != null) {
            WebSocketSession session = roomSessionMap.get(userId);
            if (session != null && session.isOpen()) {
                try {
                    String messageStr = objectMapper.writeValueAsString(message);
                    session.sendMessage(new TextMessage(messageStr));
                } catch (IOException e) {
                    log.error("发送消息给用户失败: userId={}", userId, e);
                }
            }
        }
    }
}
