package com.smartlearning.live.service;

import com.smartlearning.live.entity.ChatMessage;

import java.util.List;

/**
 * 聊天服务接口
 */
public interface ChatService {
    
    /**
     * 发送聊天消息
     */
    ChatMessage sendMessage(ChatMessage message);
    
    /**
     * 获取聊天历史
     */
    List<ChatMessage> getChatHistory(Long roomId, Integer page, Integer size);
    
    /**
     * 禁言用户
     */
    void muteUser(Long roomId, Long userId, Integer minutes);
    
    /**
     * 取消禁言
     */
    void unmuteUser(Long roomId, Long userId);
    
    /**
     * 删除消息
     */
    void deleteMessage(Long messageId);
}
