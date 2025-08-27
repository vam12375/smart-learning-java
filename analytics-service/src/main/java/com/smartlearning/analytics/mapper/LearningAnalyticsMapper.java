package com.smartlearning.analytics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartlearning.analytics.entity.LearningAnalytics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 学习分析数据Mapper
 */
@Mapper
public interface LearningAnalyticsMapper extends BaseMapper<LearningAnalytics> {
    
    /**
     * 获取用户学习概览统计
     */
    @Select("SELECT " +
            "COUNT(DISTINCT course_id) as courses_learned, " +
            "SUM(duration) as total_learning_time, " +
            "AVG(progress) as avg_progress, " +
            "AVG(score) as avg_score, " +
            "COUNT(*) as total_sessions " +
            "FROM learning_analytics " +
            "WHERE user_id = #{userId} " +
            "AND create_time >= #{startTime} " +
            "AND deleted = 0")
    Map<String, Object> selectUserLearningOverview(@Param("userId") Long userId, 
                                                  @Param("startTime") LocalDateTime startTime);
    
    /**
     * 获取课程学习统计
     */
    @Select("SELECT " +
            "COUNT(DISTINCT user_id) as student_count, " +
            "SUM(duration) as total_learning_time, " +
            "AVG(duration) as avg_learning_time, " +
            "AVG(progress) as avg_progress, " +
            "AVG(score) as avg_score, " +
            "COUNT(*) as total_sessions " +
            "FROM learning_analytics " +
            "WHERE course_id = #{courseId} " +
            "AND create_time >= #{startTime} " +
            "AND deleted = 0")
    Map<String, Object> selectCourseLearningStats(@Param("courseId") Long courseId, 
                                                 @Param("startTime") LocalDateTime startTime);
    
    /**
     * 获取学习趋势数据（按天）
     */
    @Select("SELECT " +
            "DATE(create_time) as date, " +
            "COUNT(DISTINCT user_id) as active_users, " +
            "SUM(duration) as total_learning_time, " +
            "COUNT(*) as total_sessions, " +
            "AVG(progress) as avg_progress " +
            "FROM learning_analytics " +
            "WHERE create_time >= #{startTime} " +
            "AND deleted = 0 " +
            "GROUP BY DATE(create_time) " +
            "ORDER BY date")
    List<Map<String, Object>> selectLearningTrendByDay(@Param("startTime") LocalDateTime startTime);
    
    /**
     * 获取学习趋势数据（按周）
     */
    @Select("SELECT " +
            "YEARWEEK(create_time) as week, " +
            "COUNT(DISTINCT user_id) as active_users, " +
            "SUM(duration) as total_learning_time, " +
            "COUNT(*) as total_sessions, " +
            "AVG(progress) as avg_progress " +
            "FROM learning_analytics " +
            "WHERE create_time >= #{startTime} " +
            "AND deleted = 0 " +
            "GROUP BY YEARWEEK(create_time) " +
            "ORDER BY week")
    List<Map<String, Object>> selectLearningTrendByWeek(@Param("startTime") LocalDateTime startTime);
    
    /**
     * 获取学习趋势数据（按月）
     */
    @Select("SELECT " +
            "DATE_FORMAT(create_time, '%Y-%m') as month, " +
            "COUNT(DISTINCT user_id) as active_users, " +
            "SUM(duration) as total_learning_time, " +
            "COUNT(*) as total_sessions, " +
            "AVG(progress) as avg_progress " +
            "FROM learning_analytics " +
            "WHERE create_time >= #{startTime} " +
            "AND deleted = 0 " +
            "GROUP BY DATE_FORMAT(create_time, '%Y-%m') " +
            "ORDER BY month")
    List<Map<String, Object>> selectLearningTrendByMonth(@Param("startTime") LocalDateTime startTime);
    
    /**
     * 获取热门课程排行
     */
    @Select("SELECT " +
            "course_id, " +
            "COUNT(DISTINCT user_id) as student_count, " +
            "SUM(duration) as total_learning_time, " +
            "AVG(progress) as avg_progress, " +
            "AVG(score) as avg_score, " +
            "COUNT(*) as total_sessions " +
            "FROM learning_analytics " +
            "WHERE create_time >= #{startTime} " +
            "AND deleted = 0 " +
            "GROUP BY course_id " +
            "ORDER BY student_count DESC, total_learning_time DESC " +
            "LIMIT #{limit}")
    List<Map<String, Object>> selectPopularCourses(@Param("startTime") LocalDateTime startTime, 
                                                  @Param("limit") Integer limit);
    
    /**
     * 获取用户活跃度分布
     */
    @Select("SELECT " +
            "CASE " +
            "  WHEN total_time >= 3600 THEN 'high' " +
            "  WHEN total_time >= 1800 THEN 'medium' " +
            "  ELSE 'low' " +
            "END as activity_level, " +
            "COUNT(*) as user_count " +
            "FROM ( " +
            "  SELECT user_id, SUM(duration) as total_time " +
            "  FROM learning_analytics " +
            "  WHERE create_time >= #{startTime} " +
            "  AND deleted = 0 " +
            "  GROUP BY user_id " +
            ") user_activity " +
            "GROUP BY activity_level")
    List<Map<String, Object>> selectUserActivityDistribution(@Param("startTime") LocalDateTime startTime);
    
    /**
     * 获取实时学习数据
     */
    @Select("SELECT " +
            "COUNT(DISTINCT user_id) as active_learners, " +
            "SUM(duration) as total_learning_time, " +
            "COUNT(*) as total_sessions " +
            "FROM learning_analytics " +
            "WHERE create_time >= #{startTime} " +
            "AND deleted = 0")
    Map<String, Object> selectRealtimeLearningData(@Param("startTime") LocalDateTime startTime);
    
    /**
     * 获取用户学习行为分析
     */
    @Select("SELECT " +
            "action_type, " +
            "COUNT(*) as action_count, " +
            "SUM(duration) as total_duration, " +
            "AVG(duration) as avg_duration " +
            "FROM learning_analytics " +
            "WHERE user_id = #{userId} " +
            "AND create_time >= #{startTime} " +
            "AND deleted = 0 " +
            "GROUP BY action_type")
    List<Map<String, Object>> selectUserBehaviorAnalysis(@Param("userId") Long userId, 
                                                        @Param("startTime") LocalDateTime startTime);
    
    /**
     * 获取课程章节学习分析
     */
    @Select("SELECT " +
            "chapter_id, " +
            "COUNT(DISTINCT user_id) as student_count, " +
            "SUM(duration) as total_learning_time, " +
            "AVG(progress) as avg_progress, " +
            "AVG(score) as avg_score " +
            "FROM learning_analytics " +
            "WHERE course_id = #{courseId} " +
            "AND create_time >= #{startTime} " +
            "AND deleted = 0 " +
            "GROUP BY chapter_id " +
            "ORDER BY chapter_id")
    List<Map<String, Object>> selectChapterLearningAnalysis(@Param("courseId") Long courseId, 
                                                           @Param("startTime") LocalDateTime startTime);
    
    /**
     * 获取设备使用统计
     */
    @Select("SELECT " +
            "device_type, " +
            "COUNT(DISTINCT user_id) as user_count, " +
            "COUNT(*) as session_count, " +
            "SUM(duration) as total_duration " +
            "FROM learning_analytics " +
            "WHERE create_time >= #{startTime} " +
            "AND deleted = 0 " +
            "GROUP BY device_type")
    List<Map<String, Object>> selectDeviceUsageStats(@Param("startTime") LocalDateTime startTime);
}
