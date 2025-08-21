package com.smartlearning.learning.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学习进度响应VO
 */
@Data
public class LearningProgressVO {
    
    /**
     * 主键ID
     */
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
     * 课程标题
     */
    private String courseTitle;
    
    /**
     * 课程封面
     */
    private String courseCover;
    
    /**
     * 课时ID
     */
    private Long lessonId;
    
    /**
     * 课时标题
     */
    private String lessonTitle;
    
    /**
     * 章节ID
     */
    private Long chapterId;
    
    /**
     * 章节标题
     */
    private String chapterTitle;
    
    /**
     * 学习进度百分比
     */
    private Integer progress;
    
    /**
     * 学习时长(秒)
     */
    private Integer duration;
    
    /**
     * 是否完成
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
    private LocalDateTime createTime;
}
