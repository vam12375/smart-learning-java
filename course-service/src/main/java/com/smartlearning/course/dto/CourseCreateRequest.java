package com.smartlearning.course.dto;

import lombok.Data;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * 课程创建请求DTO
 */
@Data
public class CourseCreateRequest {
    
    /**
     * 课程标题
     */
    @NotBlank(message = "课程标题不能为空")
    @Size(max = 100, message = "课程标题长度不能超过100个字符")
    private String title;
    
    /**
     * 课程副标题
     */
    @Size(max = 200, message = "课程副标题长度不能超过200个字符")
    private String subtitle;
    
    /**
     * 课程描述
     */
    @NotBlank(message = "课程描述不能为空")
    @Size(max = 2000, message = "课程描述长度不能超过2000个字符")
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
    @NotNull(message = "课程分类不能为空")
    private Long categoryId;
    
    /**
     * 课程价格
     */
    @DecimalMin(value = "0.00", message = "课程价格不能小于0")
    @Digits(integer = 8, fraction = 2, message = "价格格式不正确")
    private BigDecimal price;
    
    /**
     * 原价
     */
    @DecimalMin(value = "0.00", message = "原价不能小于0")
    @Digits(integer = 8, fraction = 2, message = "原价格式不正确")
    private BigDecimal originalPrice;
    
    /**
     * 课程难度
     */
    @NotBlank(message = "课程难度不能为空")
    @Pattern(regexp = "^(BEGINNER|INTERMEDIATE|ADVANCED)$", message = "课程难度值不正确")
    private String difficulty;
    
    /**
     * 课程标签
     */
    private List<String> tags;
    
    /**
     * 是否免费
     */
    private Integer isFree = 0;
    
    /**
     * 课程目标
     */
    @Size(max = 1000, message = "课程目标长度不能超过1000个字符")
    private String objectives;
    
    /**
     * 适合人群
     */
    @Size(max = 500, message = "适合人群长度不能超过500个字符")
    private String targetAudience;
    
    /**
     * 课程要求
     */
    @Size(max = 500, message = "课程要求长度不能超过500个字符")
    private String requirements;
    
    /**
     * 排序权重
     */
    private Integer sortOrder = 0;
    
    /**
     * 是否推荐
     */
    private Integer isRecommended = 0;
}
