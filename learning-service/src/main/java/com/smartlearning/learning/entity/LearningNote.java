package com.smartlearning.learning.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学习笔记实体类
 */
@Data
@TableName("learning_notes")
public class LearningNote {
    
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
     * 笔记标题
     */
    private String title;
    
    /**
     * 笔记内容
     */
    private String content;
    
    /**
     * 视频时间点(秒)
     */
    private Integer timePoint;
    
    /**
     * 笔记类型 TEXT:文本 IMAGE:图片 AUDIO:音频
     */
    private String type;
    
    /**
     * 是否公开 0:私有 1:公开
     */
    private Integer isPublic;
    
    /**
     * 点赞数量
     */
    private Integer likeCount;
    
    /**
     * 标签
     */
    private String tags;
    
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
