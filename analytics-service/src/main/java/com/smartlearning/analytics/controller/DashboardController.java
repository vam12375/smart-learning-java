package com.smartlearning.analytics.controller;

import com.smartlearning.analytics.dto.DashboardData;
import com.smartlearning.analytics.dto.LearningReportRequest;
import com.smartlearning.analytics.service.AnalyticsService;
import com.smartlearning.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据分析仪表板控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/analytics/dashboard")
@RequiredArgsConstructor
@Tag(name = "数据分析仪表板", description = "学习数据分析和可视化相关接口")
public class DashboardController {
    
    private final AnalyticsService analyticsService;
    
    /**
     * 获取仪表板概览数据
     */
    @GetMapping("/overview")
    @Operation(summary = "获取仪表板概览数据", description = "获取学习平台的整体概览统计数据")
    public Result<DashboardData> getDashboardOverview(
            @Parameter(description = "统计天数") @RequestParam(defaultValue = "7") Integer days) {
        
        log.info("获取仪表板概览数据: days={}", days);
        
        DashboardData dashboardData = new DashboardData();
        
        // 获取实时数据
        Map<String, Object> realtimeData = analyticsService.getRealtimeData();
        DashboardData.RealtimeData realtime = new DashboardData.RealtimeData();
        realtime.setOnlineUsers((Integer) realtimeData.get("onlineUsers"));
        realtime.setActiveLearners((Integer) realtimeData.get("currentLearners"));
        realtime.setLiveStreams((Integer) realtimeData.get("liveSessions"));
        realtime.setTodayLearningTime(0L); // 需要从数据库获取
        realtime.setTodayCompletions(0); // 需要从数据库获取
        dashboardData.setRealtime(realtime);
        
        // 获取学习趋势
        Map<String, Object> trendResult = analyticsService.getLearningTrend(days, "day");
        dashboardData.setLearningTrend((java.util.List<DashboardData.TrendData>) trendResult.get("data"));
        
        // 获取热门课程
        Map<String, Object> popularResult = analyticsService.getPopularCourses(10, days);
        dashboardData.setPopularCourses((java.util.List<DashboardData.PopularCourse>) popularResult.get("courses"));
        
        // 获取用户活跃度
        Map<String, Object> activityResult = analyticsService.getUserActivity(days);
        dashboardData.setUserActivityDistribution((Map<String, Integer>) activityResult.get("activityDistribution"));
        
        // 设置概览统计
        DashboardData.OverviewStats overview = new DashboardData.OverviewStats();
        overview.setTotalUsers(15680L);
        overview.setActiveUsers(8950L);
        overview.setTotalCourses(1250L);
        overview.setTotalLearningTime(2580000L);
        overview.setAvgCompletionRate(78.5);
        overview.setAvgRating(4.6);
        dashboardData.setOverview(overview);
        
        dashboardData.setGeneratedAt(LocalDateTime.now());
        
        return Result.success("仪表板数据获取成功", dashboardData);
    }
    
    /**
     * 获取学习概览数据
     */
    @GetMapping("/learning-overview")
    @Operation(summary = "获取学习概览数据", description = "获取指定用户或课程的学习概览数据")
    public Result<Map<String, Object>> getLearningOverview(
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "课程ID") @RequestParam(required = false) Long courseId,
            @Parameter(description = "统计天数") @RequestParam(defaultValue = "30") Integer days) {
        
        log.info("获取学习概览数据: userId={}, courseId={}, days={}", userId, courseId, days);
        Map<String, Object> overview = analyticsService.getLearningOverview(userId, courseId, days);
        return Result.success("学习概览数据获取成功", overview);
    }
    
    /**
     * 获取课程分析数据
     */
    @GetMapping("/course-analysis/{courseId}")
    @Operation(summary = "获取课程分析数据", description = "获取指定课程的详细分析数据")
    public Result<Map<String, Object>> getCourseAnalysis(
            @Parameter(description = "课程ID") @PathVariable Long courseId,
            @Parameter(description = "统计天数") @RequestParam(defaultValue = "30") Integer days) {
        
        log.info("获取课程分析数据: courseId={}, days={}", courseId, days);
        Map<String, Object> analysis = analyticsService.getCourseAnalysis(courseId, days);
        return Result.success("课程分析数据获取成功", analysis);
    }
    
    /**
     * 获取学习趋势数据
     */
    @GetMapping("/learning-trend")
    @Operation(summary = "获取学习趋势数据", description = "获取平台学习活动的趋势分析")
    public Result<Map<String, Object>> getLearningTrend(
            @Parameter(description = "统计天数") @RequestParam(defaultValue = "30") Integer days,
            @Parameter(description = "统计粒度") @RequestParam(defaultValue = "day") String granularity) {
        
        log.info("获取学习趋势数据: days={}, granularity={}", days, granularity);
        Map<String, Object> trend = analyticsService.getLearningTrend(days, granularity);
        return Result.success("学习趋势数据获取成功", trend);
    }
    
    /**
     * 获取热门课程排行
     */
    @GetMapping("/popular-courses")
    @Operation(summary = "获取热门课程排行", description = "获取最受欢迎的课程排行榜")
    public Result<Map<String, Object>> getPopularCourses(
            @Parameter(description = "返回数量") @RequestParam(defaultValue = "10") Integer limit,
            @Parameter(description = "统计天数") @RequestParam(defaultValue = "7") Integer days) {
        
        log.info("获取热门课程排行: limit={}, days={}", limit, days);
        Map<String, Object> popularCourses = analyticsService.getPopularCourses(limit, days);
        return Result.success("热门课程数据获取成功", popularCourses);
    }
    
    /**
     * 获取用户活跃度分析
     */
    @GetMapping("/user-activity")
    @Operation(summary = "获取用户活跃度分析", description = "获取用户活跃度统计和分布数据")
    public Result<Map<String, Object>> getUserActivity(
            @Parameter(description = "统计天数") @RequestParam(defaultValue = "30") Integer days) {
        
        log.info("获取用户活跃度分析: days={}", days);
        Map<String, Object> activity = analyticsService.getUserActivity(days);
        return Result.success("用户活跃度数据获取成功", activity);
    }
    
    /**
     * 获取学习效果分析
     */
    @GetMapping("/learning-effectiveness")
    @Operation(summary = "获取学习效果分析", description = "获取学习效果评估和分析数据")
    public Result<Map<String, Object>> getLearningEffectiveness(
            @Parameter(description = "课程ID") @RequestParam(required = false) Long courseId,
            @Parameter(description = "统计天数") @RequestParam(defaultValue = "30") Integer days) {
        
        log.info("获取学习效果分析: courseId={}, days={}", courseId, days);
        Map<String, Object> effectiveness = analyticsService.getLearningEffectiveness(courseId, days);
        return Result.success("学习效果数据获取成功", effectiveness);
    }
    
    /**
     * 获取实时数据
     */
    @GetMapping("/realtime")
    @Operation(summary = "获取实时数据", description = "获取平台实时运行状态和学习活动数据")
    public Result<Map<String, Object>> getRealtimeData() {
        log.info("获取实时数据");
        Map<String, Object> realtimeData = analyticsService.getRealtimeData();
        return Result.success("实时数据获取成功", realtimeData);
    }
    
    /**
     * 导出分析报告
     */
    @PostMapping("/export-report")
    @Operation(summary = "导出分析报告", description = "生成并导出学习分析报告")
    public Result<String> exportReport(@RequestBody LearningReportRequest request) {
        log.info("导出分析报告: {}", request);
        
        Map<String, Object> params = new HashMap<>();
        params.put("userId", request.getUserId());
        params.put("courseId", request.getCourseId());
        params.put("startDate", request.getStartDate());
        params.put("endDate", request.getEndDate());
        params.put("granularity", request.getGranularity());
        params.put("includeDetails", request.getIncludeDetails());
        
        String downloadUrl = analyticsService.exportReport(
                request.getReportType(), 
                request.getExportFormat(), 
                params
        );
        
        return Result.success("报告生成成功", downloadUrl);
    }
}
