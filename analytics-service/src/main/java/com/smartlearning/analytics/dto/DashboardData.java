package com.smartlearning.analytics.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 仪表板数据DTO
 */
@Data
public class DashboardData {
    
    /**
     * 概览统计
     */
    private OverviewStats overview;
    
    /**
     * 学习趋势数据
     */
    private List<TrendData> learningTrend;
    
    /**
     * 热门课程排行
     */
    private List<PopularCourse> popularCourses;
    
    /**
     * 用户活跃度分布
     */
    private Map<String, Integer> userActivityDistribution;
    
    /**
     * 实时数据
     */
    private RealtimeData realtime;
    
    /**
     * 生成时间
     */
    private LocalDateTime generatedAt;
    
    /**
     * 概览统计
     */
    @Data
    public static class OverviewStats {
        private Long totalUsers;
        private Long activeUsers;
        private Long totalCourses;
        private Long totalLearningTime;
        private Double avgCompletionRate;
        private Double avgRating;
    }
    
    /**
     * 趋势数据
     */
    @Data
    public static class TrendData {
        private String date;
        private Long activeUsers;
        private Long learningTime;
        private Long courseCompletions;
        private Long newRegistrations;
    }
    
    /**
     * 热门课程
     */
    @Data
    public static class PopularCourse {
        private Long courseId;
        private String courseName;
        private String teacherName;
        private Integer studentCount;
        private Double rating;
        private Long learningTime;
        private Double completionRate;
    }
    
    /**
     * 实时数据
     */
    @Data
    public static class RealtimeData {
        private Integer onlineUsers;
        private Integer activeLearners;
        private Integer liveStreams;
        private Long todayLearningTime;
        private Integer todayCompletions;
    }
}
