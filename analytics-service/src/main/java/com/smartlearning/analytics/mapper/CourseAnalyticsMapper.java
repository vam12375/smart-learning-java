package com.smartlearning.analytics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartlearning.analytics.entity.CourseAnalytics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 课程分析数据Mapper
 */
@Mapper
public interface CourseAnalyticsMapper extends BaseMapper<CourseAnalytics> {
    
    /**
     * 获取课程效果排行
     */
    @Select("SELECT " +
            "course_id, " +
            "course_name, " +
            "avg_completion_rate, " +
            "avg_rating, " +
            "total_students, " +
            "active_students, " +
            "completed_students " +
            "FROM course_analytics " +
            "WHERE stat_date >= #{startTime} " +
            "ORDER BY avg_completion_rate DESC, avg_rating DESC " +
            "LIMIT #{limit}")
    List<Map<String, Object>> selectCourseEffectivenessRanking(@Param("startTime") LocalDateTime startTime, 
                                                              @Param("limit") Integer limit);
    
    /**
     * 获取课程趋势分析
     */
    @Select("SELECT " +
            "DATE(stat_date) as date, " +
            "SUM(total_students) as total_students, " +
            "SUM(active_students) as active_students, " +
            "SUM(completed_students) as completed_students, " +
            "AVG(avg_completion_rate) as avg_completion_rate, " +
            "AVG(avg_rating) as avg_rating " +
            "FROM course_analytics " +
            "WHERE course_id = #{courseId} " +
            "AND stat_date >= #{startTime} " +
            "GROUP BY DATE(stat_date) " +
            "ORDER BY date")
    List<Map<String, Object>> selectCourseTrendAnalysis(@Param("courseId") Long courseId, 
                                                       @Param("startTime") LocalDateTime startTime);
    
    /**
     * 获取平台课程概览
     */
    @Select("SELECT " +
            "COUNT(DISTINCT course_id) as total_courses, " +
            "SUM(total_students) as total_enrollments, " +
            "SUM(active_students) as active_students, " +
            "SUM(completed_students) as completed_students, " +
            "AVG(avg_completion_rate) as platform_completion_rate, " +
            "AVG(avg_rating) as platform_avg_rating, " +
            "SUM(total_learning_time) as total_learning_time " +
            "FROM course_analytics " +
            "WHERE stat_date >= #{startTime}")
    Map<String, Object> selectPlatformCourseOverview(@Param("startTime") LocalDateTime startTime);
    
    /**
     * 获取课程分类统计
     */
    @Select("SELECT " +
            "c.category_name, " +
            "COUNT(ca.course_id) as course_count, " +
            "SUM(ca.total_students) as total_students, " +
            "AVG(ca.avg_completion_rate) as avg_completion_rate, " +
            "AVG(ca.avg_rating) as avg_rating " +
            "FROM course_analytics ca " +
            "LEFT JOIN courses c ON ca.course_id = c.id " +
            "WHERE ca.stat_date >= #{startTime} " +
            "GROUP BY c.category_name " +
            "ORDER BY total_students DESC")
    List<Map<String, Object>> selectCourseCategoryStats(@Param("startTime") LocalDateTime startTime);
    
    /**
     * 获取最新的课程分析数据
     */
    @Select("SELECT * FROM course_analytics " +
            "WHERE course_id = #{courseId} " +
            "ORDER BY stat_date DESC " +
            "LIMIT 1")
    CourseAnalytics selectLatestCourseAnalytics(@Param("courseId") Long courseId);
}
