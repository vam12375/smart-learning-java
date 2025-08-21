package com.smartlearning.course.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程实体类
 */
@Data
@TableName("courses")
public class Course {
    
    /**
     * 课程ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 课程标题
     */
    private String title;
    
    /**
     * 课程副标题
     */
    private String subtitle;
    
    /**
     * 课程描述
     */
    private String description;
    
    /**
     * 课程封面图片URL
     */
    private String coverImage;
    
    /**
     * 课程介绍视频URL
     */
    private String introVideo;
    
    /**
     * 课程分类ID
     */
    private Long categoryId;
    
    /**
     * 课程分类名称
     */
    private String categoryName;
    
    /**
     * 授课教师ID
     */
    private Long teacherId;
    
    /**
     * 授课教师姓名
     */
    private String teacherName;
    
    /**
     * 课程价格
     */
    private BigDecimal price;
    
    /**
     * 原价
     */
    private BigDecimal originalPrice;
    
    /**
     * 课程难度 BEGINNER:初级 INTERMEDIATE:中级 ADVANCED:高级
     */
    private String difficulty;
    
    /**
     * 课程标签(JSON格式)
     */
    private String tags;
    
    /**
     * 课程状态 DRAFT:草稿 REVIEWING:审核中 PUBLISHED:已发布 OFFLINE:已下线
     */
    private String status;
    
    /**
     * 是否免费 0:收费 1:免费
     */
    private Integer isFree;
    
    /**
     * 课程总时长(分钟)
     */
    private Integer duration;
    
    /**
     * 课时数量
     */
    private Integer lessonCount;
    
    /**
     * 学习人数
     */
    private Integer studentCount;
    
    /**
     * 收藏数量
     */
    private Integer favoriteCount;
    
    /**
     * 评分(1-5分)
     */
    private BigDecimal rating;
    
    /**
     * 评价数量
     */
    private Integer reviewCount;
    
    /**
     * 课程目标
     */
    private String objectives;
    
    /**
     * 适合人群
     */
    private String targetAudience;
    
    /**
     * 课程要求
     */
    private String requirements;
    
    /**
     * 排序权重
     */
    private Integer sortOrder;
    
    /**
     * 是否推荐 0:否 1:是
     */
    private Integer isRecommended;
    
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    
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
