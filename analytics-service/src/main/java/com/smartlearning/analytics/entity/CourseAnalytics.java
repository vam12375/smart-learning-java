package com.smartlearning.analytics.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 课程分析数据实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("course_analytics")
public class CourseAnalytics {
    
    /**
     * 分析记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 课程ID
     */
    private Long courseId;
    
    /**
     * 课程名称
     */
    private String courseName;
    
    /**
     * 统计日期
     */
    private LocalDateTime statDate;
    
    /**
     * 总学习人数
     */
    private Integer totalStudents;
    
    /**
     * 活跃学习人数
     */
    private Integer activeStudents;
    
    /**
     * 完成人数
     */
    private Integer completedStudents;
    
    /**
     * 平均学习时长（分钟）
     */
    private Double avgLearningTime;
    
    /**
     * 平均完成率
     */
    private Double avgCompletionRate;
    
    /**
     * 平均评分
     */
    private Double avgRating;
    
    /**
     * 总学习时长（分钟）
     */
    private Long totalLearningTime;
    
    /**
     * 视频观看次数
     */
    private Integer videoViews;
    
    /**
     * 练习完成次数
     */
    private Integer exerciseCompletions;
    
    /**
     * 讨论参与次数
     */
    private Integer discussionParticipations;
    
    /**
     * 资料下载次数
     */
    private Integer materialDownloads;
    
    /**
     * 退课人数
     */
    private Integer dropoutStudents;
    
    /**
     * 退课率
     */
    private Double dropoutRate;
    
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
