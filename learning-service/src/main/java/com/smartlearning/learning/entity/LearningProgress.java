package com.smartlearning.learning.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学习进度实体类
 */
@Data
@TableName("learning_progress")
public class LearningProgress {
    
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
     * 课程ID
     */
    private Long courseId;
    
    /**
     * 课时ID
     */
    private Long lessonId;
    
    /**
     * 章节ID
     */
    private Long chapterId;
    
    /**
     * 学习进度百分比(0-100)
     */
    private Integer progress;
    
    /**
     * 学习时长(秒)
     */
    private Integer duration;
    
    /**
     * 是否完成 0:未完成 1:已完成
     */
    private Integer isCompleted;
    
    /**
     * 最后学习位置(秒)
     */
    private Integer lastPosition;
    
    /**
     * 学习次数
     */
    private Integer learnCount;
    
    /**
     * 首次学习时间
     */
    private LocalDateTime firstLearnTime;
    
    /**
     * 最后学习时间
     */
    private LocalDateTime lastLearnTime;
    
    /**
     * 完成时间
     */
    private LocalDateTime completeTime;
    
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
