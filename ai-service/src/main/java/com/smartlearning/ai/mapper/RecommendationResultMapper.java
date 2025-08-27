package com.smartlearning.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartlearning.ai.entity.RecommendationResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 推荐结果Mapper
 */
@Mapper
public interface RecommendationResultMapper extends BaseMapper<RecommendationResult> {
    
    /**
     * 更新推荐结果的点击状态
     */
    @Update("UPDATE recommendation_results SET clicked = true, click_time = #{clickTime} " +
            "WHERE user_id = #{userId} AND course_id = #{courseId} AND batch_id = #{batchId}")
    int updateClickStatus(@Param("userId") Long userId, 
                         @Param("courseId") Long courseId,
                         @Param("batchId") String batchId,
                         @Param("clickTime") LocalDateTime clickTime);
    
    /**
     * 更新推荐结果的转化状态
     */
    @Update("UPDATE recommendation_results SET converted = true, convert_time = #{convertTime} " +
            "WHERE user_id = #{userId} AND course_id = #{courseId}")
    int updateConvertStatus(@Param("userId") Long userId, 
                           @Param("courseId") Long courseId,
                           @Param("convertTime") LocalDateTime convertTime);
    
    /**
     * 获取推荐效果统计
     */
    @Select("SELECT algorithm_type, " +
            "COUNT(*) as total_recommendations, " +
            "SUM(CASE WHEN clicked = true THEN 1 ELSE 0 END) as total_clicks, " +
            "SUM(CASE WHEN converted = true THEN 1 ELSE 0 END) as total_conversions, " +
            "AVG(CASE WHEN clicked = true THEN 1.0 ELSE 0.0 END) as click_rate, " +
            "AVG(CASE WHEN converted = true THEN 1.0 ELSE 0.0 END) as conversion_rate " +
            "FROM recommendation_results " +
            "WHERE create_time >= #{startTime} " +
            "GROUP BY algorithm_type")
    List<Map<String, Object>> selectRecommendationStats(@Param("startTime") LocalDateTime startTime);
    
    /**
     * 获取用户的推荐历史
     */
    @Select("SELECT * FROM recommendation_results " +
            "WHERE user_id = #{userId} " +
            "ORDER BY create_time DESC " +
            "LIMIT #{limit}")
    List<RecommendationResult> selectUserRecommendationHistory(@Param("userId") Long userId,
                                                              @Param("limit") Integer limit);
    
    /**
     * 获取课程的推荐统计
     */
    @Select("SELECT course_id, " +
            "COUNT(*) as recommendation_count, " +
            "SUM(CASE WHEN clicked = true THEN 1 ELSE 0 END) as click_count, " +
            "SUM(CASE WHEN converted = true THEN 1 ELSE 0 END) as conversion_count, " +
            "AVG(score) as avg_score " +
            "FROM recommendation_results " +
            "WHERE create_time >= #{startTime} " +
            "GROUP BY course_id " +
            "ORDER BY recommendation_count DESC " +
            "LIMIT #{limit}")
    List<Map<String, Object>> selectCourseRecommendationStats(@Param("startTime") LocalDateTime startTime,
                                                             @Param("limit") Integer limit);
    
    /**
     * 删除过期的推荐记录
     */
    @Update("DELETE FROM recommendation_results WHERE create_time < #{expireTime}")
    int deleteExpiredRecommendations(@Param("expireTime") LocalDateTime expireTime);
}
