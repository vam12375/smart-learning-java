package com.smartlearning.analytics.service;

import java.time.LocalDate;
import java.util.Map;

/**
 * 数据分析服务接口
 */
public interface AnalyticsService {

    /**
     * 获取学习概览数据
     */
    Map<String, Object> getLearningOverview(Long userId, Long courseId, Integer days);

    /**
     * 获取用户学习报告
     */
    Map<String, Object> getUserLearningReport(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 获取课程分析数据
     */
    Map<String, Object> getCourseAnalysis(Long courseId, Integer days);

    /**
     * 获取实时学习数据
     */
    Map<String, Object> getRealtimeData();

    /**
     * 获取学习趋势数据
     */
    Map<String, Object> getLearningTrend(Integer days, String granularity);

    /**
     * 获取热门课程排行
     */
    Map<String, Object> getPopularCourses(Integer limit, Integer days);

    /**
     * 获取用户活跃度分析
     */
    Map<String, Object> getUserActivity(Integer days);

    /**
     * 获取学习效果分析
     */
    Map<String, Object> getLearningEffectiveness(Long courseId, Integer days);

    /**
     * 导出分析报告
     */
    String exportReport(String reportType, String format, Map<String, Object> params);
}
