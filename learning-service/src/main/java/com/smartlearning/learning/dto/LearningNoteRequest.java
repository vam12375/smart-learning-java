package com.smartlearning.learning.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 学习笔记请求DTO
 */
@Data
public class LearningNoteRequest {
    
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
     * 笔记标题
     */
    @NotBlank(message = "笔记标题不能为空")
    @Size(max = 200, message = "笔记标题长度不能超过200个字符")
    private String title;
    
    /**
     * 笔记内容
     */
    @NotBlank(message = "笔记内容不能为空")
    @Size(max = 5000, message = "笔记内容长度不能超过5000个字符")
    private String content;
    
    /**
     * 视频时间点(秒)
     */
    private Integer timePoint;
    
    /**
     * 笔记类型
     */
    private String type = "TEXT";
    
    /**
     * 是否公开
     */
    private Integer isPublic = 0;
    
    /**
     * 标签
     */
    @Size(max = 500, message = "标签长度不能超过500个字符")
    private String tags;
}
