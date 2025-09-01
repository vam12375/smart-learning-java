package com.smartlearning.learning.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户学习统计MongoDB文档类
 */
@Data
@Document(collection = "user_learning_stats")
public class UserLearningStatsDocument {
    
    /**
     * 主键ID
     */
    @Id
    private String id;
    
    /**
     * 用户ID
     */
    @Field("userId")
    private Long userId;
    
    /**
     * 统计日期
     */
    @Field("statDate")
    private LocalDate statDate;
    
    /**
     * 学习天数
     */
    @Field("learningDays")
    private Integer learningDays;
    
    /**
     * 总学习时长（分钟）
     */
    @Field("totalLearningTime")
    private Integer totalLearningTime;
    
    /**
     * 已学课程数
     */
    @Field("coursesLearned")
    private Integer coursesLearned;
    
    /**
     * 已完成课程数
     */
    @Field("coursesCompleted")
    private Integer coursesCompleted;
    
    /**
     * 观看视频数
     */
    @Field("videosWatched")
    private Integer videosWatched;
    
    /**
     * 完成练习数
     */
    @Field("exercisesCompleted")
    private Integer exercisesCompleted;
    
    /**
     * 参与讨论数
     */
    @Field("discussionsParticipated")
    private Integer discussionsParticipated;
    
    /**
     * 下载资料数
     */
    @Field("materialsDownloaded")
    private Integer materialsDownloaded;
    
    /**
     * 平均每日学习时长（分钟）
     */
    @Field("avgDailyLearningTime")
    private Double avgDailyLearningTime;
    
    /**
     * 学习连续天数
     */
    @Field("consecutiveDays")
    private Integer consecutiveDays;
    
    /**
     * 最长连续学习天数
     */
    @Field("maxConsecutiveDays")
    private Integer maxConsecutiveDays;
    
    /**
     * 周学习目标进度（百分比）
     */
    @Field("weeklyGoalProgress")
    private Double weeklyGoalProgress;
    
    /**
     * 月学习目标进度（百分比）
     */
    @Field("monthlyGoalProgress")
    private Double monthlyGoalProgress;
    
    /**
     * 创建时间
     */
    @Field("createTime")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @Field("updateTime")
    private LocalDateTime updateTime;
}
