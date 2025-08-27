package com.smartlearning.live.service;

import com.smartlearning.live.dto.WebRTCSignalMessage;

import java.util.List;
import java.util.Map;

/**
 * WebRTC信令服务接口
 */
public interface WebRTCSignalingService {
    
    /**
     * 用户加入房间
     * @param roomId 房间ID
     * @param userId 用户ID
     * @param sessionId WebSocket会话ID
     * @param userInfo 用户信息
     */
    void joinRoom(String roomId, String userId, String sessionId, Map<String, Object> userInfo);
    
    /**
     * 用户离开房间
     * @param roomId 房间ID
     * @param userId 用户ID
     */
    void leaveRoom(String roomId, String userId);
    
    /**
     * 处理WebRTC信令消息
     * @param message 信令消息
     */
    void handleSignalMessage(WebRTCSignalMessage message);
    
    /**
     * 获取房间内的用户列表
     * @param roomId 房间ID
     * @return 用户列表
     */
    List<Map<String, Object>> getRoomUsers(String roomId);
    
    /**
     * 获取房间统计信息
     * @param roomId 房间ID
     * @return 统计信息
     */
    Map<String, Object> getRoomStats(String roomId);
    
    /**
     * 广播消息到房间
     * @param roomId 房间ID
     * @param message 消息内容
     * @param excludeUserId 排除的用户ID（可选）
     */
    void broadcastToRoom(String roomId, Object message, String excludeUserId);
    
    /**
     * 发送消息给特定用户
     * @param roomId 房间ID
     * @param userId 用户ID
     * @param message 消息内容
     */
    void sendToUser(String roomId, String userId, Object message);
    
    /**
     * 检查用户是否在房间内
     * @param roomId 房间ID
     * @param userId 用户ID
     * @return 是否在房间内
     */
    boolean isUserInRoom(String roomId, String userId);
    
    /**
     * 获取用户所在的房间列表
     * @param userId 用户ID
     * @return 房间ID列表
     */
    List<String> getUserRooms(String userId);
    
    /**
     * 清理断开连接的用户
     * @param sessionId WebSocket会话ID
     */
    void cleanupDisconnectedUser(String sessionId);
}
