package com.smartlearning.analytics.controller;

import com.smartlearning.analytics.service.AnalyticsService;
import com.smartlearning.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

/**
 * 数据分析控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Tag(name = "数据分析", description = "数据分析相关接口")
public class AnalyticsController {
    
    private final AnalyticsService analyticsService;
    
    /**
     * 获取学习概览数据
     */
    @GetMapping("/overview")
    @Operation(summary = "获取学习概览数据")
    public Result<Map<String, Object>> getLearningOverview(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(defaultValue = "7") Integer days) {
        log.info("获取学习概览数据: userId={}, courseId={}, days={}", userId, courseId, days);
        Map<String, Object> overview = analyticsService.getLearningOverview(userId, courseId, days);
        return Result.success(overview);
    }
    
    /**
     * 获取用户学习报告
     */
    @GetMapping("/user/{userId}/report")
    @Operation(summary = "获取用户学习报告")
    public Result<Map<String, Object>> getUserLearningReport(
            @PathVariable Long userId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        log.info("获取用户学习报告: userId={}, startDate={}, endDate={}", userId, startDate, endDate);
        Map<String, Object> report = analyticsService.getUserLearningReport(userId, startDate, endDate);
        return Result.success(report);
    }
    
    /**
     * 获取课程分析数据
     */
    @GetMapping("/course/{courseId}/analysis")
    @Operation(summary = "获取课程分析数据")
    public Result<Map<String, Object>> getCourseAnalysis(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "30") Integer days) {
        log.info("获取课程分析数据: courseId={}, days={}", courseId, days);
        Map<String, Object> analysis = analyticsService.getCourseAnalysis(courseId, days);
        return Result.success(analysis);
    }
    
    /**
     * 获取实时学习数据
     */
    @GetMapping("/realtime")
    @Operation(summary = "获取实时学习数据")
    public Result<Map<String, Object>> getRealtimeData() {
        log.info("获取实时学习数据");
        Map<String, Object> realtimeData = analyticsService.getRealtimeData();
        return Result.success(realtimeData);
    }
    
    /**
     * 获取学习趋势数据
     */
    @GetMapping("/trend")
    @Operation(summary = "获取学习趋势数据")
    public Result<Map<String, Object>> getLearningTrend(
            @RequestParam(defaultValue = "30") Integer days,
            @RequestParam(defaultValue = "day") String granularity) {
        log.info("获取学习趋势数据: days={}, granularity={}", days, granularity);
        Map<String, Object> trend = analyticsService.getLearningTrend(days, granularity);
        return Result.success(trend);
    }
    
    /**
     * 获取热门课程排行
     */
    @GetMapping("/popular-courses")
    @Operation(summary = "获取热门课程排行")
    public Result<Map<String, Object>> getPopularCourses(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "7") Integer days) {
        log.info("获取热门课程排行: limit={}, days={}", limit, days);
        Map<String, Object> popularCourses = analyticsService.getPopularCourses(limit, days);
        return Result.success(popularCourses);
    }
    
    /**
     * 获取用户活跃度分析
     */
    @GetMapping("/user-activity")
    @Operation(summary = "获取用户活跃度分析")
    public Result<Map<String, Object>> getUserActivity(
            @RequestParam(defaultValue = "30") Integer days) {
        log.info("获取用户活跃度分析: days={}", days);
        Map<String, Object> activity = analyticsService.getUserActivity(days);
        return Result.success(activity);
    }
    
    /**
     * 获取学习效果分析
     */
    @GetMapping("/learning-effectiveness")
    @Operation(summary = "获取学习效果分析")
    public Result<Map<String, Object>> getLearningEffectiveness(
            @RequestParam(required = false) Long courseId,
            @RequestParam(defaultValue = "30") Integer days) {
        log.info("获取学习效果分析: courseId={}, days={}", courseId, days);
        Map<String, Object> effectiveness = analyticsService.getLearningEffectiveness(courseId, days);
        return Result.success(effectiveness);
    }
    
    /**
     * 导出分析报告
     */
    @PostMapping("/export")
    @Operation(summary = "导出分析报告")
    public Result<String> exportReport(
            @RequestParam String reportType,
            @RequestParam String format,
            @RequestBody Map<String, Object> params) {
        log.info("导出分析报告: reportType={}, format={}", reportType, format);
        String downloadUrl = analyticsService.exportReport(reportType, format, params);
        return Result.success(downloadUrl);
    }
}
