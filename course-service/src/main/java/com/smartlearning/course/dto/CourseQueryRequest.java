package com.smartlearning.course.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 课程查询请求DTO
 */
@Data
public class CourseQueryRequest {
    
    /**
     * 关键词搜索
     */
    private String keyword;
    
    /**
     * 课程分类ID
     */
    private Long categoryId;
    
    /**
     * 授课教师ID
     */
    private Long teacherId;
    
    /**
     * 课程难度
     */
    private String difficulty;
    
    /**
     * 课程状态
     */
    private String status;
    
    /**
     * 是否免费
     */
    private Integer isFree;
    
    /**
     * 最低价格
     */
    private BigDecimal minPrice;
    
    /**
     * 最高价格
     */
    private BigDecimal maxPrice;
    
    /**
     * 最低评分
     */
    private BigDecimal minRating;
    
    /**
     * 是否推荐
     */
    private Integer isRecommended;
    
    /**
     * 排序字段
     */
    private String sortBy = "createTime";
    
    /**
     * 排序方向 ASC:升序 DESC:降序
     */
    private String sortOrder = "DESC";
    
    /**
     * 页码
     */
    private Integer page = 1;
    
    /**
     * 每页大小
     */
    private Integer size = 20;
}
