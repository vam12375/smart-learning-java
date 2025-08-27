package com.smartlearning.analytics.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 学习分析数据实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("learning_analytics")
public class LearningAnalytics {
    
    /**
     * 分析记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 课程ID
     */
    private Long courseId;
    
    /**
     * 章节ID
     */
    private Long chapterId;
    
    /**
     * 学习行为类型：1-观看视频，2-做练习，3-参与讨论，4-下载资料
     */
    private Integer actionType;
    
    /**
     * 学习时长（秒）
     */
    private Integer duration;
    
    /**
     * 学习进度（百分比）
     */
    private Double progress;
    
    /**
     * 学习效果评分（1-5分）
     */
    private Integer score;
    
    /**
     * 设备类型：1-PC，2-手机，3-平板
     */
    private Integer deviceType;
    
    /**
     * 浏览器类型
     */
    private String browser;
    
    /**
     * 操作系统
     */
    private String os;
    
    /**
     * IP地址
     */
    private String ipAddress;
    
    /**
     * 地理位置
     */
    private String location;
    
    /**
     * 学习开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 学习结束时间
     */
    private LocalDateTime endTime;
    
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
    
    /**
     * 是否删除
     */
    @TableLogic
    private Boolean deleted;
}
