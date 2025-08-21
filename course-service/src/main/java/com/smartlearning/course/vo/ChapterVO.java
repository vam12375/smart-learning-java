package com.smartlearning.course.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 章节响应VO
 */
@Data
public class ChapterVO {
    
    /**
     * 章节ID
     */
    private Long id;
    
    /**
     * 课程ID
     */
    private Long courseId;
    
    /**
     * 章节标题
     */
    private String title;
    
    /**
     * 章节描述
     */
    private String description;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
    
    /**
     * 状态
     */
    private Integer status;
    
    /**
     * 课时数量
     */
    private Integer lessonCount;
    
    /**
     * 章节总时长(分钟)
     */
    private Integer duration;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 课时列表
     */
    private List<LessonVO> lessons;
}
