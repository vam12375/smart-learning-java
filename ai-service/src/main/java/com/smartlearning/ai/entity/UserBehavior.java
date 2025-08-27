package com.smartlearning.ai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户行为数据实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_behaviors")
public class UserBehavior {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 行为类型：view_course, start_learning, complete_lesson, take_exam, search, etc.
     */
    private String actionType;
    
    /**
     * 目标对象ID（课程ID、课时ID、考试ID等）
     */
    private Long targetId;
    
    /**
     * 目标对象类型：course, lesson, exam, etc.
     */
    private String targetType;
    
    /**
     * 行为持续时间（秒）
     */
    private Integer duration;
    
    /**
     * 行为评分（1-5分，表示用户对内容的喜好程度）
     */
    private Integer rating;
    
    /**
     * 行为来源：recommendation, search, browse, etc.
     */
    private String source;
    
    /**
     * 设备类型：web, mobile, tablet
     */
    private String deviceType;
    
    /**
     * IP地址
     */
    private String ipAddress;
    
    /**
     * 用户代理
     */
    private String userAgent;
    
    /**
     * 扩展数据（JSON格式）
     */
    private String metadata;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
