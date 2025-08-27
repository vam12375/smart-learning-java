package com.smartlearning.ai.service;

import java.util.List;
import java.util.Map;

/**
 * 推荐算法服务接口
 */
public interface RecommendationAlgorithmService {
    
    /**
     * 协同过滤推荐
     * @param userId 用户ID
     * @param count 推荐数量
     * @return 推荐的课程ID列表及分数
     */
    List<Map<String, Object>> collaborativeFiltering(Long userId, Integer count);
    
    /**
     * 基于内容的推荐
     * @param userId 用户ID
     * @param count 推荐数量
     * @return 推荐的课程ID列表及分数
     */
    List<Map<String, Object>> contentBasedRecommendation(Long userId, Integer count);
    
    /**
     * 混合推荐算法
     * @param userId 用户ID
     * @param count 推荐数量
     * @return 推荐的课程ID列表及分数
     */
    List<Map<String, Object>> hybridRecommendation(Long userId, Integer count);
    
    /**
     * 热门课程推荐（冷启动）
     * @param count 推荐数量
     * @param categoryId 分类ID（可选）
     * @return 推荐的课程ID列表及分数
     */
    List<Map<String, Object>> popularCourseRecommendation(Integer count, Long categoryId);
    
    /**
     * 计算用户相似度
     * @param userId1 用户1
     * @param userId2 用户2
     * @return 相似度分数（0-1）
     */
    Double calculateUserSimilarity(Long userId1, Long userId2);
    
    /**
     * 计算课程相似度
     * @param courseId1 课程1
     * @param courseId2 课程2
     * @return 相似度分数（0-1）
     */
    Double calculateCourseSimilarity(Long courseId1, Long courseId2);
    
    /**
     * 更新推荐模型
     */
    void updateRecommendationModel();
}
