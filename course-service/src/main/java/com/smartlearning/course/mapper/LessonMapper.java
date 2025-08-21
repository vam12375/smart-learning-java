package com.smartlearning.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartlearning.course.entity.Lesson;
import com.smartlearning.course.vo.LessonVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课时Mapper接口
 */
@Mapper
public interface LessonMapper extends BaseMapper<Lesson> {
    
    /**
     * 根据章节ID查询课时列表
     */
    List<LessonVO> selectLessonsByChapterId(@Param("chapterId") Long chapterId);
    
    /**
     * 根据课程ID查询课时列表
     */
    List<LessonVO> selectLessonsByCourseId(@Param("courseId") Long courseId);
    
    /**
     * 增加课时学习人数
     */
    int incrementStudentCount(@Param("lessonId") Long lessonId);
    
    /**
     * 统计课程总时长
     */
    Integer sumDurationByCourseId(@Param("courseId") Long courseId);
    
    /**
     * 统计章节总时长
     */
    Integer sumDurationByChapterId(@Param("chapterId") Long chapterId);
    
    /**
     * 统计课程课时数量
     */
    Integer countLessonsByCourseId(@Param("courseId") Long courseId);
    
    /**
     * 统计章节课时数量
     */
    Integer countLessonsByChapterId(@Param("chapterId") Long chapterId);
}
