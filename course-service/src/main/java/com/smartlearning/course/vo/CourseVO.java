package com.smartlearning.course.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程响应VO
 */
@Data
public class CourseVO {
    
    /**
     * 课程ID
     */
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
     * 课程分类信息
     */
    private CategoryInfo category;
    
    /**
     * 授课教师信息
     */
    private TeacherInfo teacher;
    
    /**
     * 课程价格
     */
    private BigDecimal price;
    
    /**
     * 原价
     */
    private BigDecimal originalPrice;
    
    /**
     * 课程难度
     */
    private String difficulty;
    
    /**
     * 课程标签
     */
    private List<String> tags;
    
    /**
     * 课程状态
     */
    private String status;
    
    /**
     * 是否免费
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
     * 评分
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
     * 是否推荐
     */
    private Integer isRecommended;
    
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 课程章节列表
     */
    private List<ChapterVO> chapters;
    
    /**
     * 分类信息内部类
     */
    @Data
    public static class CategoryInfo {
        private Long id;
        private String name;
        private String icon;
    }
    
    /**
     * 教师信息内部类
     */
    @Data
    public static class TeacherInfo {
        private Long id;
        private String username;
        private String nickname;
        private String avatar;
        private String bio;
    }
}
