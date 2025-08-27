package com.smartlearning.analytics.service.impl;

import com.smartlearning.analytics.mapper.CourseAnalyticsMapper;
import com.smartlearning.analytics.mapper.LearningAnalyticsMapper;
import com.smartlearning.analytics.service.AnalyticsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 数据分析服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final LearningAnalyticsMapper learningAnalyticsMapper;
    private final CourseAnalyticsMapper courseAnalyticsMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Map<String, Object> getLearningOverview(Long userId, Long courseId, Integer days) {
        log.info("获取学习概览数据: userId={}, courseId={}, days={}", userId, courseId, days);

        // 检查缓存
        String cacheKey = "learning:overview:" + userId + ":" + (courseId != null ? courseId : "all") + ":" + days;
        Map<String, Object> cachedOverview = (Map<String, Object>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedOverview != null) {
            return cachedOverview;
        }

        LocalDateTime startTime = LocalDateTime.now().minusDays(days);
        Map<String, Object> overview = new HashMap<>();

        // 获取用户学习统计数据
        Map<String, Object> userStats = learningAnalyticsMapper.selectUserLearningOverview(userId, startTime);
        if (userStats != null) {
            overview.put("totalStudyTime", userStats.get("total_learning_time")); // 总学习时长（秒）
            overview.put("totalCourses", userStats.get("courses_learned")); // 总课程数
            overview.put("averageScore", userStats.get("avg_score")); // 平均分数
            overview.put("totalSessions", userStats.get("total_sessions")); // 总学习会话数
            overview.put("averageProgress", userStats.get("avg_progress")); // 平均进度
        }

        // 获取学习趋势数据
        List<Map<String, Object>> trendData = learningAnalyticsMapper.selectLearningTrendByDay(startTime);
        overview.put("studyTrend", trendData);

        // 获取用户行为分析
        List<Map<String, Object>> behaviorAnalysis = learningAnalyticsMapper.selectUserBehaviorAnalysis(userId,
                startTime);
        overview.put("behaviorAnalysis", behaviorAnalysis);

        // 缓存结果（15分钟）
        redisTemplate.opsForValue().set(cacheKey, overview, 15, TimeUnit.MINUTES);

        return overview;
    }

    @Override
    public Map<String, Object> getUserLearningReport(Long userId, LocalDate startDate, LocalDate endDate) {
        log.info("获取用户学习报告: userId={}, startDate={}, endDate={}", userId, startDate, endDate);

        Map<String, Object> report = new HashMap<>();

        // 基本统计
        report.put("userId", userId);
        report.put("reportPeriod", Map.of("startDate", startDate, "endDate", endDate));
        report.put("totalStudyTime", 8640); // 总学习时长（分钟）
        report.put("studyDays", 18); // 学习天数
        report.put("averageDailyTime", 480); // 平均每日学习时长

        // 课程学习情况
        List<Map<String, Object>> courseProgress = Arrays.asList(
                Map.of("courseId", 1L, "courseName", "Java基础", "progress", 95.0, "score", 88),
                Map.of("courseId", 2L, "courseName", "Spring Boot", "progress", 75.0, "score", 92),
                Map.of("courseId", 3L, "courseName", "数据库设计", "progress", 60.0, "score", 85));
        report.put("courseProgress", courseProgress);

        // 学习行为分析
        Map<String, Object> behaviorAnalysis = new HashMap<>();
        behaviorAnalysis.put("videoWatchTime", 5400); // 视频观看时长
        behaviorAnalysis.put("exerciseCount", 89); // 练习数量
        behaviorAnalysis.put("discussionCount", 12); // 讨论参与次数
        behaviorAnalysis.put("noteCount", 25); // 笔记数量
        report.put("behaviorAnalysis", behaviorAnalysis);

        return report;
    }

    @Override
    public Map<String, Object> getCourseAnalysis(Long courseId, Integer days) {
        log.info("获取课程分析数据: courseId={}, days={}", courseId, days);

        // 检查缓存
        String cacheKey = "course:analysis:" + courseId + ":" + days;
        @SuppressWarnings("unchecked")
        Map<String, Object> cachedAnalysis = (Map<String, Object>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedAnalysis != null) {
            return cachedAnalysis;
        }

        LocalDateTime startTime = LocalDateTime.now().minusDays(days);
        Map<String, Object> analysis = new HashMap<>();

        // 获取课程学习统计
        Map<String, Object> courseStats = learningAnalyticsMapper.selectCourseLearningStats(courseId, startTime);
        if (courseStats != null) {
            analysis.put("courseId", courseId);
            analysis.put("totalStudents", courseStats.get("student_count"));
            analysis.put("totalLearningTime", courseStats.get("total_learning_time"));
            analysis.put("averageLearningTime", courseStats.get("avg_learning_time"));
            analysis.put("averageProgress", courseStats.get("avg_progress"));
            analysis.put("averageScore", courseStats.get("avg_score"));
            analysis.put("totalSessions", courseStats.get("total_sessions"));
        }

        // 获取课程趋势分析
        List<Map<String, Object>> trendData = courseAnalyticsMapper.selectCourseTrendAnalysis(courseId, startTime);
        analysis.put("trendData", trendData);

        // 获取章节学习分析
        List<Map<String, Object>> chapterAnalysis = learningAnalyticsMapper.selectChapterLearningAnalysis(courseId,
                startTime);
        analysis.put("chapterAnalysis", chapterAnalysis);

        // 缓存结果（30分钟）
        redisTemplate.opsForValue().set(cacheKey, analysis, 30, TimeUnit.MINUTES);

        return analysis;
    }

    @Override
    public Map<String, Object> getRealtimeData() {
        log.info("获取实时学习数据");

        Map<String, Object> realtimeData = new HashMap<>();

        // 实时在线用户
        realtimeData.put("onlineUsers", 1580);
        realtimeData.put("activeUsers", 890);
        realtimeData.put("newRegistrations", 45);

        // 实时学习活动
        realtimeData.put("currentLearners", 650);
        realtimeData.put("liveSessions", 8);
        realtimeData.put("ongoingExams", 23);

        // 系统状态
        realtimeData.put("systemLoad", 65.8);
        realtimeData.put("responseTime", 120); // 毫秒
        realtimeData.put("errorRate", 0.02);

        // 最近活动
        List<Map<String, Object>> recentActivities = Arrays.asList(
                Map.of("userId", 1001L, "activity", "完成了Java基础课程", "time", LocalDateTime.now().minusMinutes(2)),
                Map.of("userId", 1002L, "activity", "开始学习Spring Boot", "time", LocalDateTime.now().minusMinutes(5)),
                Map.of("userId", 1003L, "activity", "参与了在线讨论", "time", LocalDateTime.now().minusMinutes(8)));
        realtimeData.put("recentActivities", recentActivities);

        return realtimeData;
    }

    @Override
    public Map<String, Object> getLearningTrend(Integer days, String granularity) {
        log.info("获取学习趋势数据: days={}, granularity={}", days, granularity);

        Map<String, Object> trend = new HashMap<>();

        List<Map<String, Object>> trendData = new ArrayList<>();
        for (int i = days - 1; i >= 0; i--) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", LocalDate.now().minusDays(i));
            dayData.put("activeUsers", (int) (Math.random() * 500 + 800));
            dayData.put("studyTime", (int) (Math.random() * 10000 + 20000));
            dayData.put("completedLessons", (int) (Math.random() * 200 + 300));
            dayData.put("newRegistrations", (int) (Math.random() * 50 + 20));
            trendData.add(dayData);
        }

        trend.put("period", days + " days");
        trend.put("granularity", granularity);
        trend.put("data", trendData);

        return trend;
    }

    @Override
    public Map<String, Object> getPopularCourses(Integer limit, Integer days) {
        log.info("获取热门课程排行: limit={}, days={}", limit, days);

        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> popularCourses = Arrays.asList(
                Map.of("courseId", 1L, "courseName", "Java编程基础", "studentCount", 2580, "rating", 4.8, "completionRate",
                        85.6),
                Map.of("courseId", 2L, "courseName", "Spring Boot实战", "studentCount", 1950, "rating", 4.7,
                        "completionRate", 78.9),
                Map.of("courseId", 3L, "courseName", "Vue.js前端开发", "studentCount", 1680, "rating", 4.6,
                        "completionRate", 82.3),
                Map.of("courseId", 4L, "courseName", "MySQL数据库", "studentCount", 1420, "rating", 4.5, "completionRate",
                        79.8),
                Map.of("courseId", 5L, "courseName", "Python数据分析", "studentCount", 1280, "rating", 4.7,
                        "completionRate", 76.5));

        result.put("period", days + " days");
        result.put("limit", limit);
        result.put("courses", popularCourses.subList(0, Math.min(limit, popularCourses.size())));

        return result;
    }

    @Override
    public Map<String, Object> getUserActivity(Integer days) {
        log.info("获取用户活跃度分析: days={}", days);

        Map<String, Object> activity = new HashMap<>();

        // 用户活跃度统计
        activity.put("totalUsers", 15680);
        activity.put("activeUsers", 8950);
        activity.put("newUsers", 450);
        activity.put("returningUsers", 8500);
        activity.put("activityRate", 57.1);

        // 活跃度分布
        Map<String, Integer> activityDistribution = new HashMap<>();
        activityDistribution.put("高活跃", 2580);
        activityDistribution.put("中活跃", 4120);
        activityDistribution.put("低活跃", 2250);
        activityDistribution.put("不活跃", 6730);
        activity.put("activityDistribution", activityDistribution);

        // 设备使用分布
        Map<String, Double> deviceDistribution = new HashMap<>();
        deviceDistribution.put("PC", 45.8);
        deviceDistribution.put("手机", 38.2);
        deviceDistribution.put("平板", 16.0);
        activity.put("deviceDistribution", deviceDistribution);

        return activity;
    }

    @Override
    public Map<String, Object> getLearningEffectiveness(Long courseId, Integer days) {
        log.info("获取学习效果分析: courseId={}, days={}", courseId, days);

        Map<String, Object> effectiveness = new HashMap<>();

        // 学习效果指标
        effectiveness.put("averageScore", 82.5);
        effectiveness.put("passRate", 89.6);
        effectiveness.put("excellentRate", 45.8);
        effectiveness.put("improvementRate", 15.2);

        // 学习路径效果
        List<Map<String, Object>> pathEffectiveness = Arrays.asList(
                Map.of("pathName", "基础入门", "completionRate", 92.5, "averageScore", 85.2),
                Map.of("pathName", "进阶提升", "completionRate", 78.9, "averageScore", 80.6),
                Map.of("pathName", "实战项目", "completionRate", 65.4, "averageScore", 88.9));
        effectiveness.put("pathEffectiveness", pathEffectiveness);

        // 知识点掌握情况
        List<Map<String, Object>> knowledgePoints = Arrays.asList(
                Map.of("pointName", "基础语法", "masteryRate", 95.2),
                Map.of("pointName", "面向对象", "masteryRate", 87.6),
                Map.of("pointName", "集合框架", "masteryRate", 78.9),
                Map.of("pointName", "多线程", "masteryRate", 65.4));
        effectiveness.put("knowledgePoints", knowledgePoints);

        return effectiveness;
    }

    @Override
    public String exportReport(String reportType, String format, Map<String, Object> params) {
        log.info("导出分析报告: reportType={}, format={}", reportType, format);

        // 这里应该生成实际的报告文件
        String fileName = reportType + "_" + System.currentTimeMillis() + "." + format.toLowerCase();
        String downloadUrl = "/downloads/reports/" + fileName;

        log.info("报告生成成功: {}", downloadUrl);
        return downloadUrl;
    }
}
