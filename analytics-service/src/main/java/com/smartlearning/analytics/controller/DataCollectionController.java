package com.smartlearning.analytics.controller;

import com.smartlearning.analytics.entity.LearningAnalytics;
import com.smartlearning.analytics.entity.UserBehavior;
import com.smartlearning.analytics.service.DataCollectionService;
import com.smartlearning.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据收集控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/analytics/collect")
@RequiredArgsConstructor
@Tag(name = "数据收集", description = "数据收集相关接口")
public class DataCollectionController {
    
    private final DataCollectionService dataCollectionService;
    
    /**
     * 收集学习行为数据
     */
    @PostMapping("/learning")
    @Operation(summary = "收集学习行为数据")
    public Result<Void> collectLearningData(@RequestBody LearningAnalytics learningData) {
        log.info("收集学习行为数据: userId={}, courseId={}, actionType={}", 
                learningData.getUserId(), learningData.getCourseId(), learningData.getActionType());
        dataCollectionService.collectLearningData(learningData);
        return Result.success();
    }
    
    /**
     * 批量收集学习行为数据
     */
    @PostMapping("/learning/batch")
    @Operation(summary = "批量收集学习行为数据")
    public Result<Void> batchCollectLearningData(@RequestBody List<LearningAnalytics> learningDataList) {
        log.info("批量收集学习行为数据: count={}", learningDataList.size());
        dataCollectionService.batchCollectLearningData(learningDataList);
        return Result.success();
    }
    
    /**
     * 收集用户行为数据
     */
    @PostMapping("/behavior")
    @Operation(summary = "收集用户行为数据")
    public Result<Void> collectUserBehavior(@RequestBody UserBehavior behaviorData) {
        log.info("收集用户行为数据: userId={}, behaviorType={}, pagePath={}", 
                behaviorData.getUserId(), behaviorData.getBehaviorType(), behaviorData.getPagePath());
        dataCollectionService.collectUserBehavior(behaviorData);
        return Result.success();
    }
    
    /**
     * 批量收集用户行为数据
     */
    @PostMapping("/behavior/batch")
    @Operation(summary = "批量收集用户行为数据")
    public Result<Void> batchCollectUserBehavior(@RequestBody List<UserBehavior> behaviorDataList) {
        log.info("批量收集用户行为数据: count={}", behaviorDataList.size());
        dataCollectionService.batchCollectUserBehavior(behaviorDataList);
        return Result.success();
    }
    
    /**
     * 收集页面访问数据
     */
    @PostMapping("/pageview")
    @Operation(summary = "收集页面访问数据")
    public Result<Void> collectPageView(
            @RequestParam Long userId,
            @RequestParam String pagePath,
            @RequestParam String pageTitle,
            @RequestParam(required = false) String referrer,
            @RequestParam(required = false) Integer stayTime) {
        log.info("收集页面访问数据: userId={}, pagePath={}", userId, pagePath);
        dataCollectionService.collectPageView(userId, pagePath, pageTitle, referrer, stayTime);
        return Result.success();
    }
    
    /**
     * 收集事件数据
     */
    @PostMapping("/event")
    @Operation(summary = "收集事件数据")
    public Result<Void> collectEvent(
            @RequestParam Long userId,
            @RequestParam String eventType,
            @RequestParam String eventAction,
            @RequestParam(required = false) String eventLabel,
            @RequestParam(required = false) Integer eventValue) {
        log.info("收集事件数据: userId={}, eventType={}, eventAction={}", userId, eventType, eventAction);
        dataCollectionService.collectEvent(userId, eventType, eventAction, eventLabel, eventValue);
        return Result.success();
    }
}
