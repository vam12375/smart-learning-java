package com.smartlearning.learning.dto;

import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 学习进度请求DTO
 */
@Data
public class LearningProgressRequest {
    
    /**
     * 课程ID
     */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;
    
    /**
     * 课时ID
     */
    @NotNull(message = "课时ID不能为空")
    private Long lessonId;
    
    /**
     * 章节ID
     */
    private Long chapterId;
    
    /**
     * 学习进度百分比(0-100)
     */
    @Min(value = 0, message = "进度不能小于0")
    @Max(value = 100, message = "进度不能大于100")
    private Integer progress;
    
    /**
     * 学习时长(秒)
     */
    @Min(value = 0, message = "学习时长不能小于0")
    private Integer duration;
    
    /**
     * 最后学习位置(秒)
     */
    @Min(value = 0, message = "学习位置不能小于0")
    private Integer lastPosition;
    
    /**
     * 是否完成
     */
    private Integer isCompleted;
}
