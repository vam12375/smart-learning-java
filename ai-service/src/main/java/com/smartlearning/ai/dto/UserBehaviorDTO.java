package com.smartlearning.ai.dto;

import lombok.Data;

/**
 * 用户行为数据传输对象
 */
@Data
public class UserBehaviorDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 行为类型
     */
    private String actionType;

    /**
     * 目标对象ID
     */
    private Long targetId;

    /**
     * 目标对象类型
     */
    private String targetType;

    /**
     * 行为持续时间（秒）
     */
    private Integer duration;

    /**
     * 行为评分（1-5分）
     */
    private Integer rating;

    /**
     * 行为来源
     */
    private String source;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 扩展数据
     */
    private String metadata;
}
