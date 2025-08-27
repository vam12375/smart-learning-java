package com.smartlearning.analytics.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户行为分析实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_behavior")
public class UserBehavior {
    
    /**
     * 行为记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 会话ID
     */
    private String sessionId;
    
    /**
     * 页面路径
     */
    private String pagePath;
    
    /**
     * 页面标题
     */
    private String pageTitle;
    
    /**
     * 行为类型：1-页面访问，2-点击，3-搜索，4-下载，5-分享
     */
    private Integer behaviorType;
    
    /**
     * 行为描述
     */
    private String behaviorDesc;
    
    /**
     * 目标对象ID（如课程ID、资源ID等）
     */
    private Long targetId;
    
    /**
     * 目标对象类型
     */
    private String targetType;
    
    /**
     * 停留时间（秒）
     */
    private Integer stayTime;
    
    /**
     * 来源页面
     */
    private String referrer;
    
    /**
     * 用户代理
     */
    private String userAgent;
    
    /**
     * 设备信息
     */
    private String deviceInfo;
    
    /**
     * 屏幕分辨率
     */
    private String screenResolution;
    
    /**
     * IP地址
     */
    private String ipAddress;
    
    /**
     * 地理位置
     */
    private String location;
    
    /**
     * 行为发生时间
     */
    private LocalDateTime behaviorTime;
    
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
