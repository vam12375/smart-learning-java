package com.smartlearning.ai.dto;

import lombok.Data;

/**
 * 推荐请求DTO
 */
@Data
public class RecommendationRequest {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 推荐数量
     */
    private Integer count = 10;

    /**
     * 推荐算法类型：collaborative_filtering, content_based, hybrid
     */
    private String algorithmType = "hybrid";

    /**
     * 课程分类ID（可选，用于限定推荐范围）
     */
    private Long categoryId;

    /**
     * 难度级别（可选）
     */
    private String difficulty;

    /**
     * 价格范围最小值（可选）
     */
    private Double minPrice;

    /**
     * 价格范围最大值（可选）
     */
    private Double maxPrice;

    /**
     * 是否排除已学习的课程
     */
    private Boolean excludeLearned = true;

    /**
     * 是否排除已购买的课程
     */
    private Boolean excludePurchased = true;
}
