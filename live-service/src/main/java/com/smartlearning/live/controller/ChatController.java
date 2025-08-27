package com.smartlearning.live.controller;

import com.smartlearning.common.result.Result;
import com.smartlearning.live.entity.ChatMessage;
import com.smartlearning.live.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 聊天控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/live/chat")
@RequiredArgsConstructor
@Tag(name = "直播聊天", description = "直播聊天相关接口")
public class ChatController {
    
    private final ChatService chatService;
    
    /**
     * 发送聊天消息
     */
    @PostMapping("/send")
    @Operation(summary = "发送聊天消息")
    public Result<ChatMessage> sendMessage(@RequestBody ChatMessage message) {
        log.info("发送聊天消息: roomId={}, userId={}, content={}", 
                message.getRoomId(), message.getUserId(), message.getContent());
        ChatMessage sent = chatService.sendMessage(message);
        return Result.success(sent);
    }
    
    /**
     * 获取聊天历史
     */
    @GetMapping("/history/{roomId}")
    @Operation(summary = "获取聊天历史")
    public Result<List<ChatMessage>> getChatHistory(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "50") Integer size) {
        List<ChatMessage> messages = chatService.getChatHistory(roomId, page, size);
        return Result.success(messages);
    }
    
    /**
     * 禁言用户
     */
    @PostMapping("/mute")
    @Operation(summary = "禁言用户")
    public Result<Void> muteUser(@RequestParam Long roomId, @RequestParam Long userId, @RequestParam Integer minutes) {
        log.info("禁言用户: roomId={}, userId={}, minutes={}", roomId, userId, minutes);
        chatService.muteUser(roomId, userId, minutes);
        return Result.success();
    }
    
    /**
     * 取消禁言
     */
    @PostMapping("/unmute")
    @Operation(summary = "取消禁言")
    public Result<Void> unmuteUser(@RequestParam Long roomId, @RequestParam Long userId) {
        log.info("取消禁言: roomId={}, userId={}", roomId, userId);
        chatService.unmuteUser(roomId, userId);
        return Result.success();
    }
    
    /**
     * 删除消息
     */
    @DeleteMapping("/{messageId}")
    @Operation(summary = "删除消息")
    public Result<Void> deleteMessage(@PathVariable Long messageId) {
        log.info("删除消息: messageId={}", messageId);
        chatService.deleteMessage(messageId);
        return Result.success();
    }
}
