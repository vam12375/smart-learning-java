package com.smartlearning.learning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartlearning.learning.entity.LearningProgress;
import com.smartlearning.learning.vo.LearningProgressVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学习进度Mapper接口
 */
@Mapper
public interface LearningProgressMapper extends BaseMapper<LearningProgress> {
    
    /**
     * 根据用户ID和课程ID查询学习进度
     */
    LearningProgress selectByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
    /**
     * 根据用户ID和课时ID查询学习进度
     */
    LearningProgress selectByUserAndLesson(@Param("userId") Long userId, @Param("lessonId") Long lessonId);
    
    /**
     * 分页查询用户学习进度
     */
    IPage<LearningProgressVO> selectUserProgressPage(Page<LearningProgressVO> page, @Param("userId") Long userId);
    
    /**
     * 查询用户课程学习进度列表
     */
    List<LearningProgressVO> selectCourseProgress(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
    /**
     * 统计用户完成的课时数
     */
    Integer countCompletedLessons(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
    /**
     * 统计用户学习的课程数
     */
    Integer countLearnedCourses(@Param("userId") Long userId);
    
    /**
     * 统计用户总学习时长
     */
    Integer sumLearningDuration(@Param("userId") Long userId);
    
    /**
     * 查询最近学习的课程
     */
    List<LearningProgressVO> selectRecentLearning(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    /**
     * 更新学习进度
     */
    int updateProgress(@Param("userId") Long userId, 
                      @Param("lessonId") Long lessonId,
                      @Param("progress") Integer progress,
                      @Param("duration") Integer duration,
                      @Param("lastPosition") Integer lastPosition,
                      @Param("isCompleted") Integer isCompleted);
}
