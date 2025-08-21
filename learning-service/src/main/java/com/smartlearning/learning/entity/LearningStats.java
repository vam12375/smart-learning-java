package com.smartlearning.learning.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学习统计实体类
 */
@Data
@TableName("learning_stats")
public class LearningStats {
    
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
     * 统计日期
     */
    private LocalDate statDate;
    
    /**
     * 学习时长(分钟)
     */
    private Integer learnDuration;
    
    /**
     * 完成课时数
     */
    private Integer completedLessons;
    
    /**
     * 学习课程数
     */
    private Integer learnCourses;
    
    /**
     * 新增笔记数
     */
    private Integer newNotes;
    
    /**
     * 连续学习天数
     */
    private Integer continuousDays;
    
    /**
     * 学习积分
     */
    private Integer points;
    
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
