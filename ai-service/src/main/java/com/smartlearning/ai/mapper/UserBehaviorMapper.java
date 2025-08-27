package com.smartlearning.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartlearning.ai.entity.UserBehavior;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户行为数据Mapper
 */
@Mapper
public interface UserBehaviorMapper extends BaseMapper<UserBehavior> {
    
    /**
     * 获取用户最近的行为数据
     */
    @Select("SELECT * FROM user_behaviors WHERE user_id = #{userId} " +
            "AND create_time >= #{startTime} ORDER BY create_time DESC LIMIT #{limit}")
    List<UserBehavior> selectRecentBehaviors(@Param("userId") Long userId, 
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("limit") Integer limit);
    
    /**
     * 获取用户对课程的交互数据
     */
    @Select("SELECT target_id as course_id, " +
            "COUNT(*) as interaction_count, " +
            "AVG(CASE WHEN rating IS NOT NULL THEN rating ELSE 0 END) as avg_rating, " +
            "SUM(CASE WHEN duration IS NOT NULL THEN duration ELSE 0 END) as total_duration " +
            "FROM user_behaviors " +
            "WHERE user_id = #{userId} AND target_type = 'course' " +
            "GROUP BY target_id")
    List<Map<String, Object>> selectUserCourseInteractions(@Param("userId") Long userId);
    
    /**
     * 获取课程的用户交互统计
     */
    @Select("SELECT user_id, " +
            "COUNT(*) as interaction_count, " +
            "AVG(CASE WHEN rating IS NOT NULL THEN rating ELSE 0 END) as avg_rating, " +
            "SUM(CASE WHEN duration IS NOT NULL THEN duration ELSE 0 END) as total_duration " +
            "FROM user_behaviors " +
            "WHERE target_id = #{courseId} AND target_type = 'course' " +
            "GROUP BY user_id")
    List<Map<String, Object>> selectCourseUserInteractions(@Param("courseId") Long courseId);
    
    /**
     * 获取用户-课程交互矩阵数据
     */
    @Select("SELECT user_id, target_id as course_id, " +
            "COUNT(*) as interaction_count, " +
            "AVG(CASE WHEN rating IS NOT NULL THEN rating ELSE 3 END) as avg_rating " +
            "FROM user_behaviors " +
            "WHERE target_type = 'course' " +
            "GROUP BY user_id, target_id " +
            "HAVING interaction_count >= #{minInteractions}")
    List<Map<String, Object>> selectUserCourseMatrix(@Param("minInteractions") Integer minInteractions);
    
    /**
     * 获取相似用户（基于行为相似度）
     */
    @Select("SELECT b2.user_id, COUNT(*) as common_courses " +
            "FROM user_behaviors b1 " +
            "JOIN user_behaviors b2 ON b1.target_id = b2.target_id AND b1.target_type = b2.target_type " +
            "WHERE b1.user_id = #{userId} AND b2.user_id != #{userId} " +
            "AND b1.target_type = 'course' AND b2.target_type = 'course' " +
            "GROUP BY b2.user_id " +
            "HAVING common_courses >= #{minCommonCourses} " +
            "ORDER BY common_courses DESC " +
            "LIMIT #{limit}")
    List<Map<String, Object>> selectSimilarUsers(@Param("userId") Long userId,
                                                @Param("minCommonCourses") Integer minCommonCourses,
                                                @Param("limit") Integer limit);
    
    /**
     * 获取用户已学习的课程ID列表
     */
    @Select("SELECT DISTINCT target_id FROM user_behaviors " +
            "WHERE user_id = #{userId} AND target_type = 'course' " +
            "AND action_type IN ('start_learning', 'complete_lesson')")
    List<Long> selectUserLearnedCourses(@Param("userId") Long userId);
    
    /**
     * 统计行为数据
     */
    @Select("SELECT action_type, COUNT(*) as count " +
            "FROM user_behaviors " +
            "WHERE create_time >= #{startTime} " +
            "GROUP BY action_type")
    List<Map<String, Object>> selectBehaviorStats(@Param("startTime") LocalDateTime startTime);
}
