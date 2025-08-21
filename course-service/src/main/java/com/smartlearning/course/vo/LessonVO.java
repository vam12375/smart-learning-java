package com.smartlearning.course.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课时响应VO
 */
@Data
public class LessonVO {
    
    /**
     * 课时ID
     */
    private Long id;
    
    /**
     * 课程ID
     */
    private Long courseId;
    
    /**
     * 章节ID
     */
    private Long chapterId;
    
    /**
     * 课时标题
     */
    private String title;
    
    /**
     * 课时描述
     */
    private String description;
    
    /**
     * 课时类型
     */
    private String type;
    
    /**
     * 视频URL
     */
    private String videoUrl;
    
    /**
     * 视频时长(秒)
     */
    private Integer duration;
    
    /**
     * 文档URL
     */
    private String documentUrl;
    
    /**
     * 课时内容
     */
    private String content;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
    
    /**
     * 是否免费试看
     */
    private Integer isFree;
    
    /**
     * 状态
     */
    private Integer status;
    
    /**
     * 学习人数
     */
    private Integer studentCount;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
