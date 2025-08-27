package com.smartlearning.ai.service.impl;

import com.smartlearning.ai.mapper.UserBehaviorMapper;
import com.smartlearning.ai.service.RecommendationAlgorithmService;
import com.smartlearning.common.feign.CourseServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 推荐算法服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationAlgorithmServiceImpl implements RecommendationAlgorithmService {

    private final UserBehaviorMapper userBehaviorMapper;
    private final CourseServiceClient courseServiceClient;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Map<String, Object>> collaborativeFiltering(Long userId, Integer count) {
        log.info("执行协同过滤推荐: userId={}, count={}", userId, count);

        // 1. 获取相似用户
        List<Map<String, Object>> similarUsers = userBehaviorMapper.selectSimilarUsers(userId, 2, 20);
        if (similarUsers.isEmpty()) {
            log.info("未找到相似用户，返回热门推荐");
            return popularCourseRecommendation(count, null);
        }

        // 2. 获取用户已学习的课程
        List<Long> learnedCourses = userBehaviorMapper.selectUserLearnedCourses(userId);
        Set<Long> learnedCourseSet = new HashSet<>(learnedCourses);

        // 3. 计算推荐分数
        Map<Long, Double> courseScores = new HashMap<>();

        for (Map<String, Object> similarUser : similarUsers) {
            Long similarUserId = (Long) similarUser.get("user_id");
            Integer commonCourses = (Integer) similarUser.get("common_courses");

            // 相似度权重
            double similarity = Math.min(1.0, commonCourses / 10.0);

            // 获取相似用户的课程交互数据
            List<Map<String, Object>> userInteractions = userBehaviorMapper.selectUserCourseInteractions(similarUserId);

            for (Map<String, Object> interaction : userInteractions) {
                Long courseId = (Long) interaction.get("course_id");

                // 跳过已学习的课程
                if (learnedCourseSet.contains(courseId)) {
                    continue;
                }

                Integer interactionCount = (Integer) interaction.get("interaction_count");
                Double avgRating = (Double) interaction.get("avg_rating");

                // 计算推荐分数
                double score = similarity * interactionCount * (avgRating / 5.0);
                courseScores.merge(courseId, score, Double::sum);
            }
        }

        // 4. 排序并返回top N
        return courseScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(count)
                .map(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("course_id", entry.getKey());
                    result.put("score", entry.getValue());
                    result.put("reason", "基于相似用户的学习偏好");
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> contentBasedRecommendation(Long userId, Integer count) {
        log.info("执行基于内容的推荐: userId={}, count={}", userId, count);

        // 1. 获取用户的课程交互数据
        List<Map<String, Object>> userInteractions = userBehaviorMapper.selectUserCourseInteractions(userId);
        if (userInteractions.isEmpty()) {
            return popularCourseRecommendation(count, null);
        }

        // 2. 分析用户偏好
        Map<String, Double> categoryPreferences = new HashMap<>();
        Map<String, Double> difficultyPreferences = new HashMap<>();

        for (Map<String, Object> interaction : userInteractions) {
            Long courseId = (Long) interaction.get("course_id");
            Double avgRating = (Double) interaction.get("avg_rating");
            Integer interactionCount = (Integer) interaction.get("interaction_count");

            // 获取课程信息（通过Feign调用课程服务）
            // 这里简化处理，实际应该调用课程服务获取课程详情
            // 基于评分和交互次数计算偏好权重
            double weight = avgRating * Math.log(interactionCount + 1);

            // 这里应该根据课程信息更新分类和难度偏好
            // 简化处理，使用模拟数据
            categoryPreferences.merge("编程", weight, Double::sum);
            difficultyPreferences.merge("INTERMEDIATE", weight, Double::sum);
        }

        // 3. 基于偏好推荐相似课程
        // 这里应该调用课程服务，根据用户偏好搜索相似课程
        // 简化处理，返回模拟推荐结果
        List<Map<String, Object>> recommendations = new ArrayList<>();

        // 模拟推荐结果
        for (int i = 1; i <= count; i++) {
            Map<String, Object> recommendation = new HashMap<>();
            recommendation.put("course_id", (long) (1000 + i));
            recommendation.put("score", 0.8 - (i * 0.05));
            recommendation.put("reason", "基于您的学习偏好和课程内容相似度");
            recommendations.add(recommendation);
        }

        return recommendations;
    }

    @Override
    public List<Map<String, Object>> hybridRecommendation(Long userId, Integer count) {
        log.info("执行混合推荐算法: userId={}, count={}", userId, count);

        // 检查缓存
        String cacheKey = "recommendation:hybrid:" + userId + ":" + count;
        List<Map<String, Object>> cachedResult = (List<Map<String, Object>>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedResult != null) {
            return cachedResult;
        }

        // 1. 获取协同过滤推荐（权重60%）
        List<Map<String, Object>> cfRecommendations = collaborativeFiltering(userId, count * 2);

        // 2. 获取基于内容的推荐（权重40%）
        List<Map<String, Object>> cbRecommendations = contentBasedRecommendation(userId, count * 2);

        // 3. 合并和重新评分
        Map<Long, Double> hybridScores = new HashMap<>();
        Map<Long, String> reasons = new HashMap<>();

        // 处理协同过滤结果
        for (Map<String, Object> rec : cfRecommendations) {
            Long courseId = (Long) rec.get("course_id");
            Double score = (Double) rec.get("score");
            hybridScores.put(courseId, score * 0.6);
            reasons.put(courseId, "协同过滤推荐");
        }

        // 处理基于内容的结果
        for (Map<String, Object> rec : cbRecommendations) {
            Long courseId = (Long) rec.get("course_id");
            Double score = (Double) rec.get("score");
            hybridScores.merge(courseId, score * 0.4, Double::sum);

            if (reasons.containsKey(courseId)) {
                reasons.put(courseId, "混合推荐算法");
            } else {
                reasons.put(courseId, "内容相似度推荐");
            }
        }

        // 4. 排序并返回结果
        List<Map<String, Object>> result = hybridScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(count)
                .map(entry -> {
                    Map<String, Object> recommendation = new HashMap<>();
                    recommendation.put("course_id", entry.getKey());
                    recommendation.put("score", entry.getValue());
                    recommendation.put("reason", reasons.get(entry.getKey()));
                    return recommendation;
                })
                .collect(Collectors.toList());

        // 缓存结果（30分钟）
        redisTemplate.opsForValue().set(cacheKey, result, 30, TimeUnit.MINUTES);

        return result;
    }

    @Override
    public List<Map<String, Object>> popularCourseRecommendation(Integer count, Long categoryId) {
        log.info("执行热门课程推荐: count={}, categoryId={}", count, categoryId);

        String cacheKey = "recommendation:popular:" + count + ":" + (categoryId != null ? categoryId : "all");
        List<Map<String, Object>> cachedResult = (List<Map<String, Object>>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedResult != null) {
            return cachedResult;
        }

        // 这里应该调用课程服务获取热门课程
        // 简化处理，返回模拟数据
        List<Map<String, Object>> recommendations = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            Map<String, Object> recommendation = new HashMap<>();
            recommendation.put("course_id", (long) (2000 + i));
            recommendation.put("score", 0.9 - (i * 0.02));
            recommendation.put("reason", "热门课程推荐");
            recommendations.add(recommendation);
        }

        // 缓存结果（1小时）
        redisTemplate.opsForValue().set(cacheKey, recommendations, 1, TimeUnit.HOURS);

        return recommendations;
    }

    @Override
    public Double calculateUserSimilarity(Long userId1, Long userId2) {
        // 获取两个用户的课程交互数据
        List<Map<String, Object>> user1Interactions = userBehaviorMapper.selectUserCourseInteractions(userId1);
        List<Map<String, Object>> user2Interactions = userBehaviorMapper.selectUserCourseInteractions(userId2);

        // 构建用户-课程评分向量
        Map<Long, Double> user1Ratings = user1Interactions.stream()
                .collect(Collectors.toMap(
                        m -> (Long) m.get("course_id"),
                        m -> (Double) m.get("avg_rating")));

        Map<Long, Double> user2Ratings = user2Interactions.stream()
                .collect(Collectors.toMap(
                        m -> (Long) m.get("course_id"),
                        m -> (Double) m.get("avg_rating")));

        // 计算余弦相似度
        Set<Long> commonCourses = new HashSet<>(user1Ratings.keySet());
        commonCourses.retainAll(user2Ratings.keySet());

        if (commonCourses.isEmpty()) {
            return 0.0;
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (Long courseId : commonCourses) {
            double rating1 = user1Ratings.get(courseId);
            double rating2 = user2Ratings.get(courseId);

            dotProduct += rating1 * rating2;
            norm1 += rating1 * rating1;
            norm2 += rating2 * rating2;
        }

        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    @Override
    public Double calculateCourseSimilarity(Long courseId1, Long courseId2) {
        // 获取两个课程的用户交互数据
        List<Map<String, Object>> course1Interactions = userBehaviorMapper.selectCourseUserInteractions(courseId1);
        List<Map<String, Object>> course2Interactions = userBehaviorMapper.selectCourseUserInteractions(courseId2);

        // 构建课程-用户评分向量
        Map<Long, Double> course1Ratings = course1Interactions.stream()
                .collect(Collectors.toMap(
                        m -> (Long) m.get("user_id"),
                        m -> (Double) m.get("avg_rating")));

        Map<Long, Double> course2Ratings = course2Interactions.stream()
                .collect(Collectors.toMap(
                        m -> (Long) m.get("user_id"),
                        m -> (Double) m.get("avg_rating")));

        // 计算余弦相似度
        Set<Long> commonUsers = new HashSet<>(course1Ratings.keySet());
        commonUsers.retainAll(course2Ratings.keySet());

        if (commonUsers.isEmpty()) {
            return 0.0;
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (Long userId : commonUsers) {
            double rating1 = course1Ratings.get(userId);
            double rating2 = course2Ratings.get(userId);

            dotProduct += rating1 * rating2;
            norm1 += rating1 * rating1;
            norm2 += rating2 * rating2;
        }

        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    @Override
    public void updateRecommendationModel() {
        log.info("开始更新推荐模型");

        try {
            // 1. 清除相关缓存
            Set<String> keys = redisTemplate.keys("recommendation:*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("清除推荐缓存: {} 个key", keys.size());
            }

            // 2. 预计算热门课程
            popularCourseRecommendation(20, null);

            // 3. 预计算用户相似度矩阵（可选，数据量大时考虑异步处理）
            // 这里可以添加更复杂的模型训练逻辑

            log.info("推荐模型更新完成");

        } catch (Exception e) {
            log.error("更新推荐模型失败", e);
        }
    }
}
