package com.smartlearning.live.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 直播会话实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("live_sessions")
public class LiveSession {
    
    /**
     * 会话ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 直播间ID
     */
    private Long roomId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 用户角色：teacher-主播，student-学生
     */
    private String role;
    
    /**
     * 会话状态：connected-已连接，disconnected-已断开
     */
    private String status;
    
    /**
     * WebSocket会话ID
     */
    private String sessionId;
    
    /**
     * 用户IP地址
     */
    private String ipAddress;
    
    /**
     * 用户代理
     */
    private String userAgent;
    
    /**
     * 连接时间
     */
    private LocalDateTime connectTime;
    
    /**
     * 断开时间
     */
    private LocalDateTime disconnectTime;
    
    /**
     * 在线时长（秒）
     */
    private Integer duration;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
