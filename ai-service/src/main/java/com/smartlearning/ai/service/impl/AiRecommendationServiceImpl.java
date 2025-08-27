package com.smartlearning.ai.service.impl;

import com.smartlearning.ai.dto.RecommendationRequest;
import com.smartlearning.ai.dto.RecommendationResponse;
import com.smartlearning.ai.dto.UserBehaviorDTO;
import com.smartlearning.ai.entity.RecommendationResult;
import com.smartlearning.ai.entity.UserBehavior;
import com.smartlearning.ai.mapper.RecommendationResultMapper;
import com.smartlearning.ai.mapper.UserBehaviorMapper;
import com.smartlearning.ai.service.AiRecommendationService;
import com.smartlearning.ai.service.RecommendationAlgorithmService;
import com.smartlearning.common.feign.CourseServiceClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AI推荐服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiRecommendationServiceImpl implements AiRecommendationService {

    private final UserBehaviorMapper userBehaviorMapper;
    private final RecommendationResultMapper recommendationResultMapper;
    private final RecommendationAlgorithmService algorithmService;
    private final CourseServiceClient courseServiceClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordUserBehavior(UserBehaviorDTO behaviorDTO) {
        try {
            UserBehavior behavior = new UserBehavior();
            BeanUtils.copyProperties(behaviorDTO, behavior);

            // 设置请求信息
            behavior.setIpAddress("127.0.0.1"); // 简化处理
            behavior.setUserAgent("Smart Learning Platform");
            behavior.setCreateTime(LocalDateTime.now());

            int result = userBehaviorMapper.insert(behavior);

            log.info("记录用户行为: userId={}, actionType={}, targetId={}, result={}",
                    behaviorDTO.getUserId(), behaviorDTO.getActionType(),
                    behaviorDTO.getTargetId(), result > 0 ? "成功" : "失败");

            return result > 0;

        } catch (Exception e) {
            log.error("记录用户行为失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchRecordUserBehavior(List<UserBehaviorDTO> behaviors) {
        int successCount = 0;

        for (UserBehaviorDTO behaviorDTO : behaviors) {
            if (recordUserBehavior(behaviorDTO)) {
                successCount++;
            }
        }

        log.info("批量记录用户行为: 总数={}, 成功={}", behaviors.size(), successCount);
        return successCount;
    }

    @Override
    public RecommendationResponse getPersonalizedRecommendations(RecommendationRequest request) {
        log.info("获取个性化推荐: userId={}, count={}, algorithmType={}",
                request.getUserId(), request.getCount(), request.getAlgorithmType());

        // 1. 根据算法类型获取推荐结果
        List<Map<String, Object>> algorithmResults;

        switch (request.getAlgorithmType()) {
            case "collaborative_filtering":
                algorithmResults = algorithmService.collaborativeFiltering(request.getUserId(), request.getCount());
                break;
            case "content_based":
                algorithmResults = algorithmService.contentBasedRecommendation(request.getUserId(), request.getCount());
                break;
            case "hybrid":
            default:
                algorithmResults = algorithmService.hybridRecommendation(request.getUserId(), request.getCount());
                break;
        }

        // 2. 生成推荐批次ID
        String batchId = UUID.randomUUID().toString();

        // 3. 获取课程详细信息并构建响应
        List<RecommendationResponse.RecommendedCourse> recommendations = new ArrayList<>();

        for (int i = 0; i < algorithmResults.size(); i++) {
            Map<String, Object> algorithmResult = algorithmResults.get(i);
            Long courseId = (Long) algorithmResult.get("course_id");
            Double score = (Double) algorithmResult.get("score");
            String reason = (String) algorithmResult.get("reason");

            // 获取课程信息（这里应该调用课程服务）
            RecommendationResponse.RecommendedCourse course = buildRecommendedCourse(courseId, score, reason, i + 1);
            if (course != null) {
                recommendations.add(course);

                // 保存推荐结果
                saveRecommendationResult(request.getUserId(), courseId, request.getAlgorithmType(),
                        score, reason, i + 1, batchId);
            }
        }

        // 4. 构建响应
        RecommendationResponse response = new RecommendationResponse();
        response.setUserId(request.getUserId());
        response.setRecommendations(recommendations);
        response.setAlgorithmType(request.getAlgorithmType());
        response.setBatchId(batchId);

        return response;
    }

    @Override
    public List<RecommendationResponse.RecommendedCourse> getSimilarCourses(Long courseId, Integer count) {
        log.info("获取相似课程推荐: courseId={}, count={}", courseId, count);

        // 这里应该实现基于课程内容的相似度计算
        // 简化处理，返回模拟数据
        List<RecommendationResponse.RecommendedCourse> similarCourses = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            RecommendationResponse.RecommendedCourse course = new RecommendationResponse.RecommendedCourse();
            course.setCourseId((long) (courseId + i));
            course.setTitle("相似课程 " + i);
            course.setDescription("与当前课程相似的课程内容");
            course.setPrice(99.0 + i * 10);
            course.setRating(4.5 - i * 0.1);
            course.setStudentCount(1000 - i * 50);
            course.setScore(0.9 - i * 0.05);
            course.setReason("基于课程内容相似度");
            course.setPosition(i);

            similarCourses.add(course);
        }

        return similarCourses;
    }

    @Override
    public List<RecommendationResponse.RecommendedCourse> getPopularCourses(Long categoryId, Integer count) {
        log.info("获取热门课程推荐: categoryId={}, count={}", categoryId, count);

        List<Map<String, Object>> popularResults = algorithmService.popularCourseRecommendation(count, categoryId);

        return popularResults.stream()
                .map(result -> {
                    Long courseId = (Long) result.get("course_id");
                    Double score = (Double) result.get("score");
                    String reason = (String) result.get("reason");

                    return buildRecommendedCourse(courseId, score, reason,
                            popularResults.indexOf(result) + 1);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordRecommendationClick(Long userId, Long courseId, String batchId) {
        try {
            int result = recommendationResultMapper.updateClickStatus(userId, courseId, batchId, LocalDateTime.now());

            log.info("记录推荐点击: userId={}, courseId={}, batchId={}, result={}",
                    userId, courseId, batchId, result > 0 ? "成功" : "失败");

            return result > 0;

        } catch (Exception e) {
            log.error("记录推荐点击失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordRecommendationConversion(Long userId, Long courseId) {
        try {
            int result = recommendationResultMapper.updateConvertStatus(userId, courseId, LocalDateTime.now());

            log.info("记录推荐转化: userId={}, courseId={}, result={}",
                    userId, courseId, result > 0 ? "成功" : "失败");

            return result > 0;

        } catch (Exception e) {
            log.error("记录推荐转化失败", e);
            return false;
        }
    }

    @Override
    public Map<String, Object> getRecommendationStats(Integer days) {
        LocalDateTime startTime = LocalDateTime.now().minusDays(days);
        List<Map<String, Object>> stats = recommendationResultMapper.selectRecommendationStats(startTime);

        Map<String, Object> result = new HashMap<>();
        result.put("period_days", days);
        result.put("algorithm_stats", stats);
        result.put("generated_at", LocalDateTime.now());

        return result;
    }

    @Override
    public Map<String, Object> getUserBehaviorStats(Long userId, Integer days) {
        LocalDateTime startTime = LocalDateTime.now().minusDays(days);
        List<UserBehavior> behaviors = userBehaviorMapper.selectRecentBehaviors(userId, startTime, 1000);

        Map<String, Object> result = new HashMap<>();
        result.put("user_id", userId);
        result.put("period_days", days);
        result.put("total_behaviors", behaviors.size());

        // 按行为类型统计
        Map<String, Long> actionStats = behaviors.stream()
                .collect(Collectors.groupingBy(UserBehavior::getActionType, Collectors.counting()));
        result.put("action_stats", actionStats);

        // 按目标类型统计
        Map<String, Long> targetStats = behaviors.stream()
                .collect(Collectors.groupingBy(UserBehavior::getTargetType, Collectors.counting()));
        result.put("target_stats", targetStats);

        result.put("generated_at", LocalDateTime.now());

        return result;
    }

    @Override
    public boolean updateRecommendationModel() {
        try {
            algorithmService.updateRecommendationModel();
            return true;
        } catch (Exception e) {
            log.error("更新推荐模型失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cleanExpiredData(Integer days) {
        LocalDateTime expireTime = LocalDateTime.now().minusDays(days);

        // 清理过期的推荐结果
        int deletedRecommendations = recommendationResultMapper.deleteExpiredRecommendations(expireTime);

        log.info("清理过期数据: 推荐结果={} 条", deletedRecommendations);

        return deletedRecommendations;
    }

    /**
     * 构建推荐课程对象
     */
    private RecommendationResponse.RecommendedCourse buildRecommendedCourse(Long courseId, Double score,
            String reason, Integer position) {
        try {
            // 这里应该调用课程服务获取课程详情
            // 简化处理，返回模拟数据
            RecommendationResponse.RecommendedCourse course = new RecommendationResponse.RecommendedCourse();
            course.setCourseId(courseId);
            course.setTitle("推荐课程 " + courseId);
            course.setDescription("这是一门优质的在线课程");
            course.setCoverImage("/images/course-" + courseId + ".jpg");
            course.setTeacherName("讲师" + (courseId % 10));
            course.setPrice(99.0 + (courseId % 100));
            course.setRating(4.0 + (courseId % 10) * 0.1);
            course.setStudentCount(1000 + (int) (courseId % 5000));
            course.setScore(score);
            course.setReason(reason);
            course.setPosition(position);

            return course;

        } catch (Exception e) {
            log.error("构建推荐课程对象失败: courseId={}", courseId, e);
            return null;
        }
    }

    /**
     * 保存推荐结果
     */
    private void saveRecommendationResult(Long userId, Long courseId, String algorithmType,
            Double score, String reason, Integer position, String batchId) {
        try {
            RecommendationResult result = new RecommendationResult();
            result.setUserId(userId);
            result.setCourseId(courseId);
            result.setAlgorithmType(algorithmType);
            result.setScore(score);
            result.setReason(reason);
            result.setPosition(position);
            result.setBatchId(batchId);
            result.setClicked(false);
            result.setConverted(false);
            result.setCreateTime(LocalDateTime.now());
            result.setUpdateTime(LocalDateTime.now());

            recommendationResultMapper.insert(result);

        } catch (Exception e) {
            log.error("保存推荐结果失败", e);
        }
    }

}
