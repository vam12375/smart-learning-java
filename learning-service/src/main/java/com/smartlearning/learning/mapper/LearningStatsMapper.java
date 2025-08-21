package com.smartlearning.learning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartlearning.learning.entity.LearningStats;
import com.smartlearning.learning.vo.LearningStatsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 学习统计Mapper接口
 */
@Mapper
public interface LearningStatsMapper extends BaseMapper<LearningStats> {
    
    /**
     * 根据用户ID和日期查询统计
     */
    LearningStats selectByUserAndDate(@Param("userId") Long userId, @Param("statDate") LocalDate statDate);
    
    /**
     * 查询用户学习统计
     */
    LearningStatsVO selectUserStats(@Param("userId") Long userId);
    
    /**
     * 查询最近N天的学习统计
     */
    List<LearningStatsVO.DailyStats> selectRecentStats(@Param("userId") Long userId, @Param("days") Integer days);
    
    /**
     * 统计用户总学习时长
     */
    Integer sumTotalDuration(@Param("userId") Long userId);
    
    /**
     * 统计用户本周学习时长
     */
    Integer sumWeekDuration(@Param("userId") Long userId, @Param("startDate") LocalDate startDate);
    
    /**
     * 统计用户本月学习时长
     */
    Integer sumMonthDuration(@Param("userId") Long userId, @Param("startDate") LocalDate startDate);
    
    /**
     * 查询用户学习排名
     */
    Integer selectUserRanking(@Param("userId") Long userId);
    
    /**
     * 更新每日统计
     */
    int updateDailyStats(@Param("userId") Long userId,
                        @Param("statDate") LocalDate statDate,
                        @Param("learnDuration") Integer learnDuration,
                        @Param("completedLessons") Integer completedLessons,
                        @Param("learnCourses") Integer learnCourses,
                        @Param("newNotes") Integer newNotes,
                        @Param("points") Integer points);
}
