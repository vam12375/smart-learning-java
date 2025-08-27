package com.smartlearning.live.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 聊天消息实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("chat_message")
public class ChatMessage {
    
    /**
     * 消息ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 直播间ID
     */
    private Long roomId;
    
    /**
     * 发送者用户ID
     */
    private Long userId;
    
    /**
     * 发送者用户名
     */
    private String username;
    
    /**
     * 发送者头像
     */
    private String avatar;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 消息类型：1-普通消息，2-系统消息，3-礼物消息
     */
    private Integer messageType;
    
    /**
     * 是否被禁言
     */
    private Boolean muted;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 是否删除
     */
    @TableLogic
    private Boolean deleted;
}
