package com.smartlearning.ai.service;

import com.smartlearning.ai.dto.RecommendationRequest;
import com.smartlearning.ai.dto.RecommendationResponse;
import com.smartlearning.ai.dto.UserBehaviorDTO;
import com.smartlearning.ai.entity.UserBehavior;

import java.util.List;
import java.util.Map;

/**
 * AI推荐服务接口
 */
public interface AiRecommendationService {
    
    /**
     * 记录用户行为
     * @param behaviorDTO 用户行为数据
     * @return 是否记录成功
     */
    boolean recordUserBehavior(UserBehaviorDTO behaviorDTO);
    
    /**
     * 批量记录用户行为
     * @param behaviors 用户行为数据列表
     * @return 成功记录的数量
     */
    int batchRecordUserBehavior(List<UserBehaviorDTO> behaviors);
    
    /**
     * 获取个性化推荐
     * @param request 推荐请求
     * @return 推荐结果
     */
    RecommendationResponse getPersonalizedRecommendations(RecommendationRequest request);
    
    /**
     * 获取相似课程推荐
     * @param courseId 课程ID
     * @param count 推荐数量
     * @return 相似课程列表
     */
    List<RecommendationResponse.RecommendedCourse> getSimilarCourses(Long courseId, Integer count);
    
    /**
     * 获取热门课程推荐
     * @param categoryId 分类ID（可选）
     * @param count 推荐数量
     * @return 热门课程列表
     */
    List<RecommendationResponse.RecommendedCourse> getPopularCourses(Long categoryId, Integer count);
    
    /**
     * 记录推荐点击
     * @param userId 用户ID
     * @param courseId 课程ID
     * @param batchId 推荐批次ID
     * @return 是否记录成功
     */
    boolean recordRecommendationClick(Long userId, Long courseId, String batchId);
    
    /**
     * 记录推荐转化
     * @param userId 用户ID
     * @param courseId 课程ID
     * @return 是否记录成功
     */
    boolean recordRecommendationConversion(Long userId, Long courseId);
    
    /**
     * 获取推荐效果统计
     * @param days 统计天数
     * @return 推荐效果数据
     */
    Map<String, Object> getRecommendationStats(Integer days);
    
    /**
     * 获取用户行为统计
     * @param userId 用户ID
     * @param days 统计天数
     * @return 用户行为统计数据
     */
    Map<String, Object> getUserBehaviorStats(Long userId, Integer days);
    
    /**
     * 更新推荐模型
     * @return 是否更新成功
     */
    boolean updateRecommendationModel();
    
    /**
     * 清理过期数据
     * @param days 保留天数
     * @return 清理的记录数
     */
    int cleanExpiredData(Integer days);
}
