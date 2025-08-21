package com.smartlearning.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户统计实体类
 */
@Data
@TableName("user_stats")
public class UserStats {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 总学习时长(分钟)
     */
    private Integer totalStudyTime;
    
    /**
     * 已报名课程数
     */
    private Integer enrolledCourses;
    
    /**
     * 已完成课程数
     */
    private Integer completedCourses;
    
    /**
     * 已完成课时数
     */
    private Integer completedLessons;
    
    /**
     * 学习笔记数
     */
    private Integer noteCount;
    
    /**
     * 收藏课程数
     */
    private Integer favoriteCount;
    
    /**
     * 考试次数
     */
    private Integer examCount;
    
    /**
     * 考试通过次数
     */
    private Integer examPassCount;
    
    /**
     * 平均考试分数
     */
    private Double averageScore;
    
    /**
     * 总积分
     */
    private Integer totalPoints;
    
    /**
     * 当前等级
     */
    private Integer currentLevel;
    
    /**
     * 连续学习天数
     */
    private Integer continuousDays;
    
    /**
     * 最长连续学习天数
     */
    private Integer maxContinuousDays;
    
    /**
     * 最后学习时间
     */
    private LocalDateTime lastStudyTime;
    
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
     * 逻辑删除标识
     */
    @TableLogic
    private Integer deleted;
}
