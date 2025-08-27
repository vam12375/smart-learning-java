package com.smartlearning.ai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 推荐结果实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("recommendation_results")
public class RecommendationResult {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 推荐的课程ID
     */
    private Long courseId;
    
    /**
     * 推荐算法类型：collaborative_filtering, content_based, hybrid
     */
    private String algorithmType;
    
    /**
     * 推荐分数
     */
    private Double score;
    
    /**
     * 推荐原因
     */
    private String reason;
    
    /**
     * 推荐位置（排序）
     */
    private Integer position;
    
    /**
     * 是否被点击
     */
    private Boolean clicked;
    
    /**
     * 点击时间
     */
    private LocalDateTime clickTime;
    
    /**
     * 是否被购买/学习
     */
    private Boolean converted;
    
    /**
     * 转化时间
     */
    private LocalDateTime convertTime;
    
    /**
     * 推荐批次ID
     */
    private String batchId;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
