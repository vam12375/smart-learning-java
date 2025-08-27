package com.smartlearning.ai.controller;

import com.smartlearning.ai.dto.RecommendationRequest;
import com.smartlearning.ai.dto.RecommendationResponse;
import com.smartlearning.ai.dto.UserBehaviorDTO;
import com.smartlearning.ai.service.AiRecommendationService;
import com.smartlearning.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * AI推荐控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI推荐服务", description = "智能推荐相关接口")
public class AiRecommendationController {

    private final AiRecommendationService aiRecommendationService;

    /**
     * 记录用户行为
     */
    @PostMapping("/behavior")
    @Operation(summary = "记录用户行为", description = "记录用户的学习行为数据")
    public Result<Boolean> recordUserBehavior(@RequestBody UserBehaviorDTO behaviorDTO) {
        log.info("记录用户行为请求: userId={}, actionType={}", behaviorDTO.getUserId(), behaviorDTO.getActionType());
        boolean success = aiRecommendationService.recordUserBehavior(behaviorDTO);
        return Result.success("行为记录" + (success ? "成功" : "失败"), success);
    }

    /**
     * 批量记录用户行为
     */
    @PostMapping("/behavior/batch")
    @Operation(summary = "批量记录用户行为", description = "批量记录用户的学习行为数据")
    public Result<Integer> batchRecordUserBehavior(@RequestBody List<UserBehaviorDTO> behaviors) {
        log.info("批量记录用户行为请求: 数量={}", behaviors.size());
        int successCount = aiRecommendationService.batchRecordUserBehavior(behaviors);
        return Result.success("批量记录完成", successCount);
    }

    /**
     * 获取个性化推荐
     */
    @PostMapping("/recommendations")
    @Operation(summary = "获取个性化推荐", description = "根据用户行为获取个性化课程推荐")
    public Result<RecommendationResponse> getPersonalizedRecommendations(
            @RequestBody RecommendationRequest request) {
        log.info("获取个性化推荐请求: userId={}, count={}", request.getUserId(), request.getCount());
        RecommendationResponse response = aiRecommendationService.getPersonalizedRecommendations(request);
        return Result.success("推荐获取成功", response);
    }

    /**
     * 获取用户推荐（简化接口）
     */
    @GetMapping("/recommendations/{userId}")
    @Operation(summary = "获取用户推荐", description = "获取指定用户的推荐课程")
    public Result<RecommendationResponse> getUserRecommendations(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "推荐数量") @RequestParam(defaultValue = "10") Integer count,
            @Parameter(description = "算法类型") @RequestParam(defaultValue = "hybrid") String algorithmType) {

        log.info("获取用户推荐请求: userId={}, count={}, algorithmType={}", userId, count, algorithmType);

        RecommendationRequest request = new RecommendationRequest();
        request.setUserId(userId);
        request.setCount(count);
        request.setAlgorithmType(algorithmType);

        RecommendationResponse response = aiRecommendationService.getPersonalizedRecommendations(request);
        return Result.success("推荐获取成功", response);
    }

    /**
     * 获取相似课程推荐
     */
    @GetMapping("/courses/{courseId}/similar")
    @Operation(summary = "获取相似课程推荐", description = "获取与指定课程相似的课程推荐")
    public Result<List<RecommendationResponse.RecommendedCourse>> getSimilarCourses(
            @Parameter(description = "课程ID") @PathVariable Long courseId,
            @Parameter(description = "推荐数量") @RequestParam(defaultValue = "5") Integer count) {

        log.info("获取相似课程推荐请求: courseId={}, count={}", courseId, count);
        List<RecommendationResponse.RecommendedCourse> similarCourses = aiRecommendationService
                .getSimilarCourses(courseId, count);
        return Result.success("相似课程获取成功", similarCourses);
    }

    /**
     * 获取热门课程推荐
     */
    @GetMapping("/courses/popular")
    @Operation(summary = "获取热门课程推荐", description = "获取热门课程推荐列表")
    public Result<List<RecommendationResponse.RecommendedCourse>> getPopularCourses(
            @Parameter(description = "分类ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "推荐数量") @RequestParam(defaultValue = "10") Integer count) {

        log.info("获取热门课程推荐请求: categoryId={}, count={}", categoryId, count);
        List<RecommendationResponse.RecommendedCourse> popularCourses = aiRecommendationService
                .getPopularCourses(categoryId, count);
        return Result.success("热门课程获取成功", popularCourses);
    }

    /**
     * 记录推荐点击
     */
    @PostMapping("/recommendations/click")
    @Operation(summary = "记录推荐点击", description = "记录用户点击推荐课程的行为")
    public Result<Boolean> recordRecommendationClick(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "课程ID") @RequestParam Long courseId,
            @Parameter(description = "推荐批次ID") @RequestParam String batchId) {

        log.info("记录推荐点击请求: userId={}, courseId={}, batchId={}", userId, courseId, batchId);
        boolean success = aiRecommendationService.recordRecommendationClick(userId, courseId, batchId);
        return Result.success("点击记录" + (success ? "成功" : "失败"), success);
    }

    /**
     * 记录推荐转化
     */
    @PostMapping("/recommendations/conversion")
    @Operation(summary = "记录推荐转化", description = "记录用户购买/学习推荐课程的转化行为")
    public Result<Boolean> recordRecommendationConversion(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "课程ID") @RequestParam Long courseId) {

        log.info("记录推荐转化请求: userId={}, courseId={}", userId, courseId);
        boolean success = aiRecommendationService.recordRecommendationConversion(userId, courseId);
        return Result.success("转化记录" + (success ? "成功" : "失败"), success);
    }

    /**
     * 获取推荐效果统计
     */
    @GetMapping("/stats/recommendations")
    @Operation(summary = "获取推荐效果统计", description = "获取推荐算法的效果统计数据")
    public Result<Map<String, Object>> getRecommendationStats(
            @Parameter(description = "统计天数") @RequestParam(defaultValue = "7") Integer days) {

        log.info("获取推荐效果统计请求: days={}", days);
        Map<String, Object> stats = aiRecommendationService.getRecommendationStats(days);
        return Result.success("统计数据获取成功", stats);
    }

    /**
     * 获取用户行为统计
     */
    @GetMapping("/stats/behavior/{userId}")
    @Operation(summary = "获取用户行为统计", description = "获取指定用户的行为统计数据")
    public Result<Map<String, Object>> getUserBehaviorStats(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "统计天数") @RequestParam(defaultValue = "30") Integer days) {

        log.info("获取用户行为统计请求: userId={}, days={}", userId, days);
        Map<String, Object> stats = aiRecommendationService.getUserBehaviorStats(userId, days);
        return Result.success("用户行为统计获取成功", stats);
    }

    /**
     * 更新推荐模型
     */
    @PostMapping("/model/update")
    @Operation(summary = "更新推荐模型", description = "手动触发推荐模型更新")
    public Result<Boolean> updateRecommendationModel() {
        log.info("更新推荐模型请求");
        boolean success = aiRecommendationService.updateRecommendationModel();
        return Result.success("模型更新" + (success ? "成功" : "失败"), success);
    }

    /**
     * 清理过期数据
     */
    @DeleteMapping("/data/cleanup")
    @Operation(summary = "清理过期数据", description = "清理过期的推荐和行为数据")
    public Result<Integer> cleanExpiredData(
            @Parameter(description = "保留天数") @RequestParam(defaultValue = "90") Integer days) {

        log.info("清理过期数据请求: days={}", days);
        int deletedCount = aiRecommendationService.cleanExpiredData(days);
        return Result.success("数据清理完成", deletedCount);
    }
}
