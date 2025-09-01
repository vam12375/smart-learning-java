package com.smartlearning.learning.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 学习笔记MongoDB文档类
 */
@Data
@Document(collection = "learning_notes")
public class LearningNoteDocument {
    
    /**
     * 主键ID
     */
    @Id
    private String id;
    
    /**
     * 用户ID
     */
    @Field("userId")
    private Long userId;
    
    /**
     * 课程ID
     */
    @Field("courseId")
    private Long courseId;
    
    /**
     * 课时ID
     */
    @Field("lessonId")
    private Long lessonId;
    
    /**
     * 笔记标题
     */
    @Field("title")
    private String title;
    
    /**
     * 笔记内容
     */
    @Field("content")
    private String content;
    
    /**
     * 视频时间点(秒)
     */
    @Field("timePoint")
    private Integer timePoint;
    
    /**
     * 笔记类型 TEXT:文本 IMAGE:图片 AUDIO:音频
     */
    @Field("type")
    private String type;
    
    /**
     * 是否公开
     */
    @Field("isPublic")
    private Boolean isPublic;
    
    /**
     * 点赞数量
     */
    @Field("likeCount")
    private Integer likeCount;
    
    /**
     * 标签列表
     */
    @Field("tags")
    private List<String> tags;
    
    /**
     * 创建时间
     */
    @Field("createTime")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @Field("updateTime")
    private LocalDateTime updateTime;
    
    /**
     * 是否删除
     */
    @Field("deleted")
    private Boolean deleted;
}
