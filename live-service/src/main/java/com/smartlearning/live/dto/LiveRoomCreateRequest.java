package com.smartlearning.live.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 创建直播间请求DTO
 */
@Data
public class LiveRoomCreateRequest {

    /**
     * 直播间标题
     */
    private String title;

    /**
     * 直播间描述
     */
    private String description;

    /**
     * 主播用户ID
     */
    private Long teacherId;

    /**
     * 课程ID（可选）
     */
    private Long courseId;

    /**
     * 直播间封面图
     */
    private String coverImage;

    /**
     * 预计开始时间
     */
    private LocalDateTime scheduledStartTime;

    /**
     * 是否允许聊天
     */
    private Boolean chatEnabled = true;

    /**
     * 是否录制
     */
    private Boolean recordEnabled = false;

    /**
     * 直播间密码（可选）
     */
    private String password;
}
