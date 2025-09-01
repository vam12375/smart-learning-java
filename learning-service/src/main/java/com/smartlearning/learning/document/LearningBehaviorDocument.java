package com.smartlearning.learning.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * 学习行为分析MongoDB文档类
 */
@Data
@Document(collection = "learning_behaviors")
public class LearningBehaviorDocument {
    
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
     * 章节ID
     */
    @Field("chapterId")
    private Long chapterId;
    
    /**
     * 学习行为类型：1-观看视频，2-做练习，3-参与讨论，4-下载资料
     */
    @Field("actionType")
    private Integer actionType;
    
    /**
     * 学习时长（秒）
     */
    @Field("duration")
    private Integer duration;
    
    /**
     * 学习进度（百分比）
     */
    @Field("progress")
    private Double progress;
    
    /**
     * 学习效果评分（1-5分）
     */
    @Field("score")
    private Integer score;
    
    /**
     * 设备类型：1-PC，2-手机，3-平板
     */
    @Field("deviceType")
    private Integer deviceType;
    
    /**
     * 浏览器类型
     */
    @Field("browser")
    private String browser;
    
    /**
     * 操作系统
     */
    @Field("os")
    private String os;
    
    /**
     * IP地址
     */
    @Field("ipAddress")
    private String ipAddress;
    
    /**
     * 地理位置
     */
    @Field("location")
    private String location;
    
    /**
     * 行为发生时间
     */
    @Field("actionTime")
    private LocalDateTime actionTime;
    
    /**
     * 创建时间
     */
    @Field("createTime")
    private LocalDateTime createTime;
}
