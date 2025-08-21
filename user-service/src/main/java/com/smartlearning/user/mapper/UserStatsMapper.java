package com.smartlearning.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartlearning.user.entity.UserStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户统计Mapper接口
 */
@Mapper
public interface UserStatsMapper extends BaseMapper<UserStats> {
    
    /**
     * 根据用户ID查询用户统计
     */
    UserStats selectByUserId(@Param("userId") Long userId);
    
    /**
     * 更新学习时长
     */
    int updateStudyTime(@Param("userId") Long userId, @Param("duration") Integer duration);
    
    /**
     * 更新课程统计
     */
    int updateCourseStats(@Param("userId") Long userId, 
                         @Param("enrolledCourses") Integer enrolledCourses,
                         @Param("completedCourses") Integer completedCourses,
                         @Param("completedLessons") Integer completedLessons);
    
    /**
     * 更新考试统计
     */
    int updateExamStats(@Param("userId") Long userId,
                       @Param("examCount") Integer examCount,
                       @Param("examPassCount") Integer examPassCount,
                       @Param("averageScore") Double averageScore);
    
    /**
     * 更新积分和等级
     */
    int updatePointsAndLevel(@Param("userId") Long userId,
                            @Param("totalPoints") Integer totalPoints,
                            @Param("currentLevel") Integer currentLevel);
    
    /**
     * 更新连续学习天数
     */
    int updateContinuousDays(@Param("userId") Long userId,
                            @Param("continuousDays") Integer continuousDays,
                            @Param("maxContinuousDays") Integer maxContinuousDays);
    
    /**
     * 初始化用户统计
     */
    int initUserStats(@Param("userId") Long userId);
}
