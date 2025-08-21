package com.smartlearning.course.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课时实体类
 */
@Data
@TableName("lessons")
public class Lesson {
    
    /**
     * 课时ID
     */
    @TableId(type = IdType.AUTO)
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
     * 课时类型 VIDEO:视频 DOCUMENT:文档 QUIZ:测验 LIVE:直播
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
     * 是否免费试看 0:否 1:是
     */
    private Integer isFree;
    
    /**
     * 状态 0:禁用 1:启用
     */
    private Integer status;
    
    /**
     * 学习人数
     */
    private Integer studentCount;
    
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
