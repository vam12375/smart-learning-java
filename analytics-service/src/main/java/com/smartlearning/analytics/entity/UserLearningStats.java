package com.smartlearning.analytics.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户学习统计实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_learning_stats")
public class UserLearningStats {
    
    /**
     * 统计记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 统计日期
     */
    private LocalDateTime statDate;
    
    /**
     * 学习天数
     */
    private Integer learningDays;
    
    /**
     * 总学习时长（分钟）
     */
    private Long totalLearningTime;
    
    /**
     * 已学课程数
     */
    private Integer coursesLearned;
    
    /**
     * 已完成课程数
     */
    private Integer coursesCompleted;
    
    /**
     * 观看视频数
     */
    private Integer videosWatched;
    
    /**
     * 完成练习数
     */
    private Integer exercisesCompleted;
    
    /**
     * 参与讨论数
     */
    private Integer discussionsParticipated;
    
    /**
     * 下载资料数
     */
    private Integer materialsDownloaded;
    
    /**
     * 平均每日学习时长（分钟）
     */
    private Double avgDailyLearningTime;
    
    /**
     * 学习连续天数
     */
    private Integer consecutiveDays;
    
    /**
     * 最长连续学习天数
     */
    private Integer maxConsecutiveDays;
    
    /**
     * 学习活跃度评分（1-5分）
     */
    private Double activityScore;
    
    /**
     * 学习效果评分（1-5分）
     */
    private Double effectivenessScore;
    
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
